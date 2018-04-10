package com.mcd.rwd.global.core.utils;

import com.adobe.granite.rest.utils.DeepModifiableValueMapDecorator;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.wrappers.ModifiableValueMapDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public final class ContentMigrationUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ContentMigrationUtil.class);

    private static final String DEFAULT_RESOURCE_TYPE = "nt:unstructured";
    private static final String CHILD_NAME_TPL = "child%s";
    private static final Map<String, Object> DEFAULT_PROPERTIES = new HashMap<>();
    static {
        DEFAULT_PROPERTIES.put("jcr:primaryType", DEFAULT_RESOURCE_TYPE);
    }
    private static final Set<String> FINAL_PROPS = new HashSet<>();
    static {
        FINAL_PROPS.add("jcr:primaryType");
        FINAL_PROPS.add("jcr:mixinTypes");
    }

    public static class MigrationRequest {

        private final String resourceType;
        private final Map<String, String> oldToNewNames = new LinkedHashMap<>();
        private final Map<String, Object> defaultValues = new LinkedHashMap<>();
        private final Map<String, Object> setValues = new LinkedHashMap<>();
        private final Map<String, String> customWidgetConversions = new LinkedHashMap<>();
        private final Map<String, String> jsonConversions = new LinkedHashMap<>();
        private final Map<String, Transformer> transformers = new LinkedHashMap<>();

        public MigrationRequest(String resourceType) {
            this.resourceType = resourceType;
        }

        public MigrationRequest rename(final String oldName, final String newName) {
            oldToNewNames.put(oldName, newName);
            return this;
        }

        public MigrationRequest defaultValue(final String propertyName, final Object defaultValue) {
            defaultValues.put(propertyName, defaultValue);
            return this;
        }

        public MigrationRequest setValue(final String propertyName, final Object defaultValue) {
            setValues.put(propertyName, defaultValue);
            return this;
        }

        public MigrationRequest convertCustomWidget(final String oldPath, final String newPath) {
            customWidgetConversions.put(oldPath, newPath);
            return this;
        }

        public MigrationRequest convertJson(final String propertyName, final String parent) {
            jsonConversions.put(propertyName, parent);
            return this;
        }

        public MigrationRequest convertJson(final String propertyName) {
            jsonConversions.put(propertyName, propertyName);
            return this;
        }

        public MigrationRequest transformValues(final String propertyName, final Transformer transformer) {
            transformers.put(propertyName, transformer);
            return this;
        }

        public String getResourceType() {
            return resourceType;
        }
    }

    public static class MigrationResponse {

        private final Map<String, String> migratedPaths = new LinkedHashMap<>();
        private final Map<String, Object> defaultsSet = new LinkedHashMap<>();
        private final Map<String, Object> valuesSet = new LinkedHashMap<>();
        private final Map<String, String> customWidgetConversions = new LinkedHashMap<>();
        private final Map<String, String> jsonConversions = new LinkedHashMap<>();

        public MigrationResponse renamed(final String oldPath, final String newPath) {
            migratedPaths.put(oldPath, newPath);
            return this;
        }

        public MigrationResponse defaultSet(final String path, final Object value) {
            defaultsSet.put(path, value);
            return this;
        }

        public MigrationResponse valueSet(final String path, final Object value) {
            valuesSet.put(path, value);
            return this;
        }

        public MigrationResponse customWidgetMoved(final String oldPath, final String newPath) {
            customWidgetConversions.put(oldPath, newPath);
            return this;
        }

        public MigrationResponse jsonConverted(final String oldPath, final String newPath) {
            jsonConversions.put(oldPath, newPath);
            return this;
        }

        public Map<String, String> getMigratedPaths() {
            return migratedPaths;
        }

        public Map<String, Object> getDefaultsSet() {
            return defaultsSet;
        }

        public Map<String, Object> getValuesSet() {
            return valuesSet;
        }

        public Map<String, String> getCustomWidgetConversions() {
            return customWidgetConversions;
        }

        public Map<String, String> getJsonConversions() {
            return jsonConversions;
        }
    }

    /**
     * Executes a content migration request, returning a response object that summarizes the result.
     * @param resolver the resolver to use (must have write permissions on content)
     * @param request the request
     * @return the response object
     */
    public static MigrationResponse migrate(final ResourceResolver resolver, final MigrationRequest request)
            throws PersistenceException {

        final Map<String, String> predicates = new HashMap<>();
        predicates.put("path", "/content");
        predicates.put("property", "sling:resourceType");
        predicates.put("property.value", request.resourceType);
        final PredicateGroup group = PredicateGroup.create(predicates);

        final QueryBuilder queryBuilder = resolver.adaptTo(QueryBuilder.class);
        final Session session = resolver.adaptTo(Session.class);
        final Query query = queryBuilder.createQuery(group, session);

        query.setHitsPerPage(0L);
        final SearchResult result = query.getResult();

        final MigrationResponse response = new MigrationResponse();
        result.getResources().forEachRemaining(resource -> migrateResource(resource, request, response));

        resolver.commit();

        return response;
    }

    private static void migrateResource(final Resource resource, final MigrationRequest request,
                                        final MigrationResponse response) {

        final AllNodeDeepModifiableValueMapDecorator valueMap
                = new AllNodeDeepModifiableValueMapDecorator(resource, resource.adaptTo(ModifiableValueMap.class));
        final String basePath = resource.getPath();
        defaultValues(valueMap, basePath, request.defaultValues, response);
        setValues(valueMap, basePath, request.setValues, response);
        renameProperties(valueMap, basePath, request.oldToNewNames, response);
        convertCustomWidgets(valueMap, basePath, request.customWidgetConversions, response);
        convertJsonPropertiesToNodes(valueMap, basePath, request.jsonConversions, response);
        transformValues(valueMap, request.transformers);
    }

    private static void defaultValues(final AllNodeDeepModifiableValueMapDecorator valueMap,
                                      final String basePath,
                                      final Map<String, Object> defaultValues,
                                      final MigrationResponse response) {

        for (Map.Entry<String, Object> entry : defaultValues.entrySet()) {
            final String name = entry.getKey();
            final Object value = entry.getValue();
            if (defaultValue(valueMap, name, value)) {
                response.defaultSet(basePath + "/" + name, value);
            }
        }
    }

    private static boolean defaultValue(final AllNodeDeepModifiableValueMapDecorator valueMap, final String name,
                                        final Object value) {

        if (valueMap.get(name) == null) {
            valueMap.put(name, value);
            return true;
        }
        return false;
    }

    private static void setValues(final AllNodeDeepModifiableValueMapDecorator valueMap,
                                  final String basePath,
                                  final Map<String, Object> setValues,
                                  final MigrationResponse response) {

        for (Map.Entry<String, Object> entry : setValues.entrySet()) {
            final String name = entry.getKey();
            final Object value = entry.getValue();
            if (!(value instanceof ValueMap)) {
                setValue(valueMap, name, value);
                response.valueSet(basePath + "/" + name, value);
            }
        }
    }

    private static void setValue(final AllNodeDeepModifiableValueMapDecorator valueMap, final String name,
                                 final Object value) {

        valueMap.put(name, value);
    }

    private static void renameProperties(final AllNodeDeepModifiableValueMapDecorator valueMap,
                                         final String basePath,
                                         final Map<String, String> oldToNewNames,
                                         final MigrationResponse response) {

        for (Map.Entry<String, String> entry : oldToNewNames.entrySet()) {
            final String oldName = entry.getKey();
            final String newName = entry.getValue();
            if (renameProperty(valueMap, oldName, newName)) {
                response.renamed(basePath + "/" + oldName, basePath + "/" + newName);
            }
        }
    }

    private static boolean renameProperty(final AllNodeDeepModifiableValueMapDecorator valueMap, final String oldName,
                                          final String newName) {

        final Object value = valueMap.get(oldName);
        if (value != null && !(value instanceof ValueMap)) {
            valueMap.remove(oldName);
            valueMap.put(newName, value);
            return true;
        }
        return false;
    }

    private static void convertCustomWidgets(final AllNodeDeepModifiableValueMapDecorator valueMap,
                                             final String basePath,
                                             final Map<String, String> oldToNewRelativePaths,
                                             final MigrationResponse response) {

        for (Map.Entry<String, String> entry : oldToNewRelativePaths.entrySet()) {
            convertCustomWidget(valueMap, basePath, entry.getKey(), entry.getValue(), response);
        }
    }

    private static void convertCustomWidget(final AllNodeDeepModifiableValueMapDecorator valueMap,
                                            final String basePath,
                                            final String oldRelativePath,
                                            final String newRelativePath,
                                            final MigrationResponse response) {

        final ValueMap customWidgetProperties = valueMap.getValueMap(oldRelativePath + "/");
        if (customWidgetProperties != null) {
            for (String name: customWidgetProperties.keySet()) {
                convertCustomWidgetProperty(valueMap, oldRelativePath, newRelativePath, name);
                response.customWidgetMoved(basePath + "/" + oldRelativePath, basePath + "/" + newRelativePath);
            }
        }
    }

    private static void convertCustomWidgetProperty(final AllNodeDeepModifiableValueMapDecorator valueMap,
                                                    final String oldRelativePath,
                                                    final String newRelativePath,
                                                    final String name) {
        final String relativePath = oldRelativePath + "/" + name;
        final Object array = valueMap.get(relativePath);
        if (array != null && array.getClass().isArray()) {
            final int length = Array.getLength(array);
            for (int i = 0; i < length; i++) {
                final String childName = String.format(CHILD_NAME_TPL, i);
                final String childPath = newRelativePath + "/" + childName;
                final Object value = Array.get(array, i);
                if (value != null) {
                    valueMap.put(childPath + "/" + name, value);
                }
            }
        }
    }

    private static void convertJsonPropertiesToNodes(final AllNodeDeepModifiableValueMapDecorator valueMap,
                                                     final String basePath,
                                                     final Map<String, String> mapping,
                                                     final MigrationResponse response) {
        for (Map.Entry<String, String> entry : mapping.entrySet()) {
            final String propertyName = entry.getKey();
            final String parent = entry.getValue();
            convertJsonToNodes(valueMap, propertyName, parent);
            response.jsonConverted(basePath + "/" + propertyName, basePath + "/" + parent);
        }
    }

    private static void convertJsonToNodes(final AllNodeDeepModifiableValueMapDecorator valueMap,
                                           final String sourcePropertyName,
                                           final String destinationParent) {
        final ObjectMapper mapper = new ObjectMapper();
        final String[] jsonItems = valueMap.get(sourcePropertyName, new String[0]);
        valueMap.remove(sourcePropertyName);
        for (int i = 0; i < jsonItems.length; i++) {
            final String jsonItem = jsonItems[i];
            try {
                final JsonNode jsonNode = mapper.readTree(jsonItem);
                final String relPath = destinationParent + "/" + String.format(CHILD_NAME_TPL, i) + "/";
                final Iterator<String> fieldNames = jsonNode.fieldNames();
                while (fieldNames.hasNext()) {
                    final String name = fieldNames.next();
                    final JsonNode jsonValue = jsonNode.get(name);
                    final Object value;
                    if (jsonValue.isBoolean()) {
                        value = jsonValue.asBoolean();
                    } else if(jsonValue.isInt()) {
                        value = jsonValue.asInt();
                    } else if (jsonValue.isDouble()) {
                        value = jsonValue.asDouble();
                    } else {
                        value = jsonValue.asText();
                    }
                    valueMap.put(relPath + name, value);
                }
            } catch (IOException e) {
                LOG.error("Error parsing JSON item", e);
            }
        }
    }

    private static void transformValues(final AllNodeDeepModifiableValueMapDecorator valueMap,
                                       final Map<String, Transformer> transformers) {
        for (Map.Entry<String, Transformer> entry : transformers.entrySet()) {
            final String starPath = entry.getKey();
            final Transformer transformer = entry.getValue();
            transformValue(valueMap, starPath, transformer);
        }
    }

    private static void transformValue(final AllNodeDeepModifiableValueMapDecorator valueMap,
                                       final String starName,
                                       final Transformer transformer) {
        final Set<String> names = valueMap.getNames(starName);
        for (String name : names) {
            valueMap.put(name, transformer.apply(valueMap.get(name)));
        }
    }

    private ContentMigrationUtil() { }

    private static class AllNodeDeepModifiableValueMapDecorator extends DeepModifiableValueMapDecorator {

        private final Resource resource;

        private static String[] getAllNodes(final Resource resource) {
            final List<String> list = new ArrayList<>();
            resource.getChildren().forEach(child -> collectNodes("", child, list));
            return list.toArray(new String[list.size()]);
        }

        private static void collectNodes(final String prefix, final Resource resource, final List<String> nodes) {
            final String nodeName = prefix + resource.getName();
            nodes.add(nodeName);
            final String childPrefix = nodeName + "/";
            resource.getChildren().forEach(child -> collectNodes(childPrefix, child, nodes));
        }

        public AllNodeDeepModifiableValueMapDecorator(Resource resource, Map<String, Object> base) {
            super(resource, base, getAllNodes(resource));
            this.resource = resource;
        }

        public Set<String> getNames(final String starPath) {
            final String nodePath = ResourceUtil.getParent(starPath);
            final String propertyName = ResourceUtil.getName(starPath);
            final String regex = "^" + nodePath.replace("*", "[^/]+") + "$";
            final String[] allNodes = getAllNodes(resource);
            final Set<String> out = new HashSet<>();
            for (String node : allNodes) {
                if (node.matches(regex)) {
                    out.add(node + "/" + propertyName);
                }
            }
            return out;
        }

        protected ValueMap getValueMap(String name) {
            int pos = name.lastIndexOf("/");
            if(pos == -1) {
                return new ModifiableValueMapDecorator(this.base);
            } else {
                final String resourceName = this.pathPrefix + name.substring(0, pos);
                Resource resource = this.resolver.resolve(resourceName);
                if(ResourceUtil.isNonExistingResource(resource)) {
                    resource = createResource(this.resolver, resourceName);
                }
                ValueMap vm = resource.adaptTo(ModifiableValueMap.class);
                if(vm != null) {
                    return new ModifiableValueMapDecorator(vm);
                }

                return ModifiableValueMap.EMPTY;
            }
        }

        private Resource createResource(final ResourceResolver resolver, final String resourcePath) {

            final String parentPath = ResourceUtil.getParent(resourcePath);
            Resource parent = resolver.resolve(parentPath);
            if (ResourceUtil.isNonExistingResource(parent)) {
                parent = createResource(resolver, parentPath);
            }

            try {
                return resolver.create(parent, ResourceUtil.getName(resourcePath), DEFAULT_PROPERTIES);
            } catch (PersistenceException e) {
                throw new IllegalStateException("Failed to create new node", e);
            }
        }

        public Object put(String key, Object value) {
            if(key == null) {
                return null;
            } else {
                return this.getValueMap(key).put(this.getPropertyName(key), value);
            }
        }

        public Object remove(String key) {
            if(key == null) {
                return null;
            } else {
                return this.getValueMap(key).remove(this.getPropertyName(key));
            }
        }
    }

    public interface Transformer {

        Object apply(Object input);

    }
}

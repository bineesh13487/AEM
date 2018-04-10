package com.mcd.rwd.global.core.models.injectors;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.components.ComponentContext;
import com.day.cq.wcm.api.designer.Design;
import com.day.cq.wcm.api.designer.Designer;
import com.day.cq.wcm.commons.WCMUtils;
import com.mcd.rwd.global.core.models.annotations.DesignAnnotation;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.spi.DisposalCallbackRegistry;
import org.apache.sling.models.spi.Injector;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;

/**
 * Created by sandeepc on 05/07/17.
 */

@Component
@Service
@Property(name = Constants.SERVICE_RANKING, intValue = 2500)
public class DesignInjector implements Injector {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesignInjector.class);

    @Override
    public String getName() {
        return "design-injector";
    }

    @Override
    public Object getValue(Object adaptable, String name, Type type, AnnotatedElement annotatedElement,
                           DisposalCallbackRegistry disposalCallbackRegistry) {
        DesignAnnotation designAnnotation= annotatedElement.getAnnotation(DesignAnnotation.class);
        if (designAnnotation == null){
            return null;
        }
        String resourceName = designAnnotation.value();

        Design currentDesign = getCurrentDesign(adaptable);
        if (null != currentDesign) {
            Resource contentResource = currentDesign.getContentResource();
            if (null != contentResource) {
                return contentResource.getChild(resourceName);
            }
        }

        return null;
    }

    private ComponentContext getComponentContext(Object adaptable) {
        if (adaptable instanceof SlingHttpServletRequest) {
            SlingHttpServletRequest request = ((SlingHttpServletRequest) adaptable);

            return WCMUtils.getComponentContext(request);
        }
        return null;
    }

    private Page getCurrentPage(Object adaptable) {
        ComponentContext context = getComponentContext(adaptable);

        return (context != null) ? context.getPage() : null;
    }

    private Design getCurrentDesign(Object adaptable) {
        Page currentPage = getCurrentPage(adaptable);
        Designer designer = getDesigner(adaptable);

        if (currentPage != null && designer != null) {
            return designer.getDesign(currentPage);
        }

        return null;
    }

    private Designer getDesigner(Object adaptable) {
        ResourceResolver resolver = getResourceResolver(adaptable);

        if (resolver != null) {
            return resolver.adaptTo(Designer.class);
        }

        return null;
    }

    private ResourceResolver getResourceResolver(Object adaptable) {
        if (adaptable instanceof SlingHttpServletRequest) {
            return ((SlingHttpServletRequest) adaptable).getResourceResolver();
        }
        if (adaptable instanceof Resource) {
            return ((Resource) adaptable).getResourceResolver();
        }

        return null;
    }

}

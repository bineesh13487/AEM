import com.mcd.rwd.global.core.utils.ContentMigrationUtil
import com.mcd.rwd.global.core.utils.ContentMigrationUtil.Transformer
import com.mcd.rwd.global.core.utils.ContentMigrationUtil.MigrationRequest
import com.mcd.rwd.global.core.utils.ContentMigrationUtil.MigrationResponse
import org.apache.sling.api.resource.ResourceResolver

def doMigration(final MigrationRequest request) {
    final ResourceResolver resolver = resourceResolver;
    final MigrationResponse response = ContentMigrationUtil.migrate(resolver, request);
    println "Migrating ${request.resourceType}"
    println "Defaulted properties for ${request.resourceType}:"
    response.defaultsSet.entrySet().forEach({e -> println " ${e.key} = ${e.value}"})
    println "Set properties for ${request.resourceType}:"
    response.valuesSet.entrySet().forEach({e -> println " ${e.key} = ${e.value}"})
    println "Moved properties for ${request.resourceType}:"
    response.migratedPaths.entrySet().forEach({e -> println " ${e.key} -> ${e.value}"})
    println "Converted customwidget instances for ${request.resourceType}:"
    response.customWidgetConversions.entrySet().forEach({e -> println " ${e.key} -> ${e.value}"})
    println "Converted JSON properties"
    response.jsonConversions.entrySet().forEach({e -> println " ${e.key} -> ${e.value}"})
    println ""
}

final MigrationRequest imageListFlyout = new MigrationRequest("mcd-us/components/content/imagelist-flyout")
imageListFlyout.convertCustomWidget("customwidget", "imageList")

doMigration(imageListFlyout)

final Transformer stripText = new Transformer() {
    Object apply(Object input) {
        if (input instanceof String) {
            String[] split = input.split("_")
            return split[0]
        }
        return input
    }
}

final MigrationRequest nutritionCalculator = new MigrationRequest("mcd-us/components/content/nutrition-calculator")
nutritionCalculator.setValue("sling:resourceType", "mcd-us/components/content/nutrition_calculator")
    .convertJson("nutrientList").transformValues("nutrientList/*/nutrientList", stripText)
    .convertJson("secondaryNutrientList").transformValues("secondaryNutrientList/*/secondaryNutrientList", stripText)
    .convertJson("categoryList").transformValues("categoryList/*/categoryList", stripText)
    .convertJson("secondaryNutrient").transformValues("secondaryNutrient/*/secondaryNutrientList", stripText)
    .convertJson("secondaryNutrient2").transformValues("secondaryNutrient2/*/secondaryNutrientList", stripText)
    .convertJson("secondaryNutrient3").transformValues("secondaryNutrient3/*/secondaryNutrientList", stripText)
    .convertJson("secondaryNutrientMobile").transformValues("secondaryNutrientMobile/*/secondaryNutrientList", stripText)
    .convertJson("secondaryNutrientMobile2").transformValues("secondaryNutrientMobile2/*/secondaryNutrientList", stripText)

doMigration(nutritionCalculator)

final MigrationRequest publication = new MigrationRequest("mcd-rwd-global/components/content/publication")
publication.rename("text", "text/text")
publication.rename("textIsRich", "text/textIsRich")

doMigration(publication)

final MigrationRequest tabs = new MigrationRequest("mcd-rwd-global/components/content/tabs")
tabs.convertJson("tabs")

doMigration(tabs)



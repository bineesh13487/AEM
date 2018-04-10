package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.*;
import com.day.cq.wcm.api.Page;
import com.icfolson.aem.multicompositeaddon.widget.MultiCompositeField;
import com.mcd.rwd.global.core.components.common.Text;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.bean.CategoryListPOJO;
import com.mcd.rwd.us.core.bean.NutritionListPOJO;
import com.mcd.rwd.us.core.bean.SecondaryNutrientListPOJO;
import com.mcd.rwd.us.core.bean.category.CategoryDetail;
import com.mcd.rwd.us.core.constants.ApplicationConstants;
import com.mcd.rwd.us.core.service.McdFactoryConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;
import com.mcd.rwd.us.core.service.NutrientProvider;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Dipankar Gupta on 3/24/2016.
 */
@Component(name = "nutrition_calculator",value = "Nutrition-calculator", actions = { "text: Nutrition-calculator", "-", "editannotate", "copymove", "delete" },
		disableTargeting = true, group = "GWS-Global" , path="/content",tabs = {@Tab(title = "basic"), @Tab(title = "Secondary Nutrients for Desktop") , @Tab(title = "Secondary Nutrients for Mobile") , @Tab(title = "Default Texts") , @Tab(title = "ADA Texts") })
@Model(adaptables = {SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NutritionCalculator {

	private static final Logger LOGGER = LoggerFactory.getLogger(NutritionCalculator.class);

	private static final String NUTRINT_MARKETING_NAME = "nutrientMarketingName";

	private static final String SECONDARY_NUTRINT_MARKETING_NAME = "secondaryNutrientMarketingName";
	
	private static final String SECONDARY_NUTRINT_LIST = "secondaryNutrientList";
	
	private static final String TWO = "two";
	
	private static final String THREE = "three";

	@DialogField(fieldLabel = "Title", required = true)
	@TextField
	@Inject
	String title;

	@DialogField(fieldLabel = "Item Image Folder Path", required = true , fieldDescription="Name of image should be attach_transparent_icon_image")
	@PathField(rootPath="/content/dam")
	@Inject
	String itemImageFolderPath;

	@DialogField(fieldLabel = "Category Default Image Folder Path", required = true)
	@PathField(rootPath="/content/dam")
	@Inject
	private String componentImageFolderPath;

	@DialogField(fieldLabel = "Please select the view type Default is Tabular View.", value="tableView", defaultValue="tableView")
	@Selection(options = {
			@Option(text = "Tabular View [Nutrition Table will appear in Table View]", value = "tableView"),
			@Option(text = "Column View [Nutrition Table will appear in Column View]", value = "columnView")
	}, type = Selection.SELECT)
	@Inject @Named("viewTypeSelection") @Default(values="tableView")
	String secondaryNutrientsViewType;

	@DialogField(fieldLabel = "Enter the Heading for Nutritions Name Column", fieldDescription="Default value is 'Nutritional Information'" )
	@TextField
	@Inject @Default(values="Nutritional Information")
	String nutritionColumnHeading;

	@DialogField(fieldLabel="Enter the Heading for Measurement Unit Column" , fieldDescription="Default value is 'Per PTN'" )
	@TextField
	@Inject @Default(values="Per PTN")
	String measurementUnitColumnHeading;

	@DialogField(fieldLabel="Enter the Heading for %DV Column" , fieldDescription="Default value is - '% DV (Adult)'" )
	@TextField
	@Inject @Default(values="% DV (Adult)")
	String percentageUnitColumnHeading;

	@DialogField( fieldDescription="Choose the categories.",fieldLabel="Category name" , title="Category")
	@MultiCompositeField
	@Inject
	List<CategoryListPOJO> categoryList;

	@DialogField( fieldDescription="Choose the nutrients...", fieldLabel=" Primary Nutrient name")
	@MultiCompositeField
	@Inject
	List<NutritionListPOJO> nutrientList = new ArrayList<>();

	@DialogField( fieldLabel="Please provide more information for nutrition table")
	@DialogFieldSet(namePrefix = "healthHyperlink/" , title = "Nutrition table" )
	@ChildResource(name = "healthHyperlink")
	@Inject
	Text healthHyperlink;

	@DialogField(fieldLabel="Select number of columns for desktop secondary nutrients. Default is three",title="Secondary Nutrients for Desktop",tab = 2 , defaultValue = "three",
		listeners = {
					@Listener(name = "selectionchanged" , value = "function( field,value, isChecked ){ " + "console.log('changed');" + "if (field !=null){ " + "console.log(value);" + "var panel = field.findParentByType('panel');" + "var col1 = panel.getComponent('secondaryNutrient1'); " + "  var col2 = panel.getComponent('secondaryNutrient2'); " + "  var col3 = panel.getComponent('secondaryNutrient3'); " + "  console.log(value); " +
						" if (value == 'one'){  " +
						" console.log('one'); " +
						" col2.hide(); " +
						" col3.hide(); " +
						" col1.show(); " +
						" }else if (value=='two'){  " +
						" col3.hide(); " +
						" col2.show(); " +
						" col1.show();  " +
						" }else if (value=='three') { " +
						" col2.show(); " +
						" col3.show(); " +
						" col1.show(); " +
						" } " +
						" else { " +
						" col2.hide(); " +
						" col3.hide(); " +
						" col1.show(); " +
						" } " +
						" }  " +
						" }"),
					@Listener(name = "loadcontent" , value = "function( ){  " +
						"  " +
						" var panel = this.findParentByType('panel'); " +
						"  var col1 = panel.getComponent('secondaryNutrient1'); " +
						"  var col2 = panel.getComponent('secondaryNutrient2'); " +
						"  var col3 = panel.getComponent('secondaryNutrient3'); " +
						"  col2.show(); " +
						" col3.show(); " +
						" col1.show(); " +
						" }")})
	@Selection(options = {
			@Option(text = "one", value = "one"),
			@Option(text = "two", value = "two"),
			@Option(text = "three", value = "three", selected = true)
	}, type = Selection.RADIO)
	@Inject @Default(values="three")
	String secondaryColCount;

	@DialogField( fieldDescription="Choose the secondary nutrients...", fieldLabel="Secondary Nutrient Col1" ,title="Secondary Nutrient Col1" , cssClass = "secondaryNutrient" ,tab = 2, required = true)
	@MultiCompositeField
	@Inject
	List<SecondaryNutrientListPOJO> secondaryNutrientList = new ArrayList<>();;

	@DialogField( fieldDescription="Choose the secondary nutrients...", fieldLabel="Secondary Nutrient Col2" ,title="Secondary Nutrient Col2" , cssClass = "secondaryNutrient2" , tab = 2)
	@MultiCompositeField
	@Inject
	List<SecondaryNutrientListPOJO> secondaryNutrientList2 = new ArrayList<>();;

	@DialogField( fieldDescription="Choose the secondary nutrients...", fieldLabel="Secondary Nutrient Col3" ,title="Secondary Nutrient Col3" , cssClass = "secondaryNutrient3" ,tab = 2)
	@MultiCompositeField
	@Inject
	List<SecondaryNutrientListPOJO> secondaryNutrientList3 = new ArrayList<>();;


	@DialogField(fieldLabel="Select number of columns for mobile secondary nutrients. Default is three",title="Secondary Nutrients for Mobile", defaultValue = "two", tab = 3,
			listeners = {
					@Listener(name = "selectionchanged" , value = "function( field,value, isChecked ){"+
							"console.log('changed');"+
							"if (field !=null){"+
							"console.log(value);"+
							"var panel = field.findParentByType('panel');"+
							"var col1 = panel.getComponent('secondaryNutrientMobile1');"+
							"var col2 = panel.getComponent('secondaryNutrientMobile2');"+
							"console.log(value);"+
							"if (value == 'one'){"+
							"console.log('one');"+
							"col2.hide();"+
							"col1.show();"+
							"}else if (value=='two'){"+
							"col2.show();"+
							"col1.show();"+
							"}else {"+
							"col2.show();"+
							"col1.show();"+
							"}"+
							"}"+
							"}"),
					@Listener(name = "loadcontent" , value = "function( ){  " +
							" var panel = this.findParentByType('panel'); " +
							" var col1 = panel.getComponent('secondaryNutrientMobile1'); " +
							" var col2 = panel.getComponent('secondaryNutrientMobile2'); " +
							" col2.show(); " +
							" col1.show(); " +
							" }")})
	@Selection(options = {
			@Option(text = "one", value = "one"),
			@Option(text = "two", value = "two", selected = true),
			@Option(text = "three", value = "three")
	}, type = Selection.RADIO)
	@Inject @Default(values="three")
	String secondaryColCountMobile;

	@DialogField( fieldDescription="Choose the secondary nutrients...", fieldLabel="Secondary Nutrient Col1" ,title="Secondary Nutrient Col1" , cssClass = "secondaryNutrientMobile", tab = 3, required = true)
	@MultiCompositeField
	@Inject
	List<SecondaryNutrientListPOJO> secondaryNutrientListMobile = new ArrayList<>();;

	@DialogField( fieldDescription="Choose the secondary nutrients...", fieldLabel="Secondary Nutrient Col2" ,title="Secondary Nutrient Col2" ,cssClass = "secondaryNutrientMobile2", tab = 3)
	@MultiCompositeField
	@Inject
	List<SecondaryNutrientListPOJO> secondaryNutrientListMobile2 = new ArrayList<>();;

	@DialogField(fieldLabel = "'item(s)' text", tab = 4)
	@TextField
	@Inject @Default(values="Item(s)")
	String itemIn;


	@DialogField(fieldLabel="'Showing Nutrition for:' text", required = true , tab = 4)
	@TextField
	@Inject @Default(values="Showing Nutrition for:")
	String myMeal;

	@DialogField( fieldLabel="'Back' button text", tab = 4)
	@TextField
	@Inject @Default(values="Back")
	String backButton;

	@DialogField(fieldLabel="'Close Ingredients' text" , tab = 4)
	@TextField
	@Inject @Default(values="Close Ingredients")
	String closeIngredients;

	@DialogField(fieldLabel="'Customize Ingredients' text" , tab = 4)
	@TextField
	@Inject
	String customizeIngredients;

	@DialogField(fieldLabel="'Close Item Details' text" , tab = 4)
	@TextField
	@Inject @Default(values="Close Item Details")
	String closeItemDetails;

	@DialogField(fieldLabel="'Explore Item Details' text" , tab = 4)
	@TextField
	@Inject @Default(values="Explore Item Details")
	String exploreItemDetails;

	@DialogField(fieldLabel="'click here' text" , tab = 4)
	@TextField
	@Inject
	String clickHere;

	@DialogField(fieldLabel="Select category text", fieldDescription="Please put '##' placeholder for 'click here' text. Default value is - 'Select from the categories below'", tab = 4)
	@DialogFieldSet(namePrefix = "selectCategoryText/" , title = "Category text" )
	@ChildResource(name = "selectCategoryText")
	@Inject @Default(values="Select from the categories below")
	Text selectCategoryText;

	@DialogField(fieldLabel="'Delete' button text (X button)" , tab = 4)
	@TextField
	@Inject @Default(values="Delete")
	String deleteButton;

	@DialogField(fieldLabel="'Delete all items' text" , tab = 4)
	@TextField
	@Inject @Default(values="Delete all items")
	String deleteAllButton;

	@DialogField(fieldLabel="'DV Disclaimer' text", fieldDescription="Default value is - '%DV = % Daily Value'" , tab = 4)
	@TextField
	@Inject @Default(values="%DV = % Daily Value")
	String dvDisclaimer;

	@DialogField(fieldLabel="'ADD MORE ITEMS' button text" , tab = 4)
	@TextField
	@Inject @Default(values="Add more items")
	String addButton;

	@DialogField(fieldLabel="'ADD MORE' button text" , tab = 4)
	@TextField
	@Inject @Default(values="Add More")
	String addMore;

	@DialogField(fieldLabel="'Added to calculator' button text" , tab = 4)
	@TextField @Default(values="Added to calculator")
	String addCalculator;

	@DialogField(fieldLabel="Youtube video id" , tab = 4)
	@TextField
	@Inject
	String videoId;

	@DialogField( fieldLabel="'VIEW YOUR ITEMS' button text" , tab = 4)
	@TextField
	@Inject @Default(values="View your items")
	String viewButton;

	@DialogField(fieldLabel="'Yes' button text in dialog" , tab = 4)
	@TextField
	@Inject @Default(values="Yes")
	String yesButton;

	@DialogField(fieldLabel="'No' button text in dialog" , tab = 4)
	@TextField
	@Inject @Default(values="No")
	String noButton;

	@DialogField(fieldLabel="'hide' text" , tab = 4)
	@TextField
	@Inject @Default(values="Hide")
	String hideText;

	@DialogField( fieldLabel="'show' text" , tab = 4)
	@TextField
	@Inject @Default(values="Show")
	String showText;

	@DialogField( fieldLabel="'Delete All Dialog' text", fieldDescription="Default value is - 'Are you sure you want to delete all of your items?'", tab = 4)
	@DialogFieldSet(namePrefix = "deleteAllDialog/" , title = "'Delete All Dialog' text" )
	@ChildResource(name = "deleteAllDialog")
	@Inject @Default(values="Are you sure you want to delete all of your items?")
	Text deleteAllDialog;

	@DialogField(fieldLabel="'DV' nutrition text" , tab = 4)
	@TextField
	@Inject @Default(values="DV")
	String dvText;

	@DialogField(fieldLabel="'Select an item' text", fieldDescription="Default value is - 'Select an item below to calculate nutrition'" , tab = 4)
	@TextField
	@Inject @Default(values="Select an item below to calculate nutrition")
	String selectItem;

	@DialogField( fieldLabel="'Delete Dialog' text", fieldDescription="Default value is - 'Are you sure you want to delete {itemName} ?'", tab = 4)
	@DialogFieldSet(namePrefix = "deleteDialog/" , title = "'Delete Dialog' text" )
	@ChildResource(name = "deleteDialog")
	@Inject @Default(values="Are you sure you want to delete {itemName} ?")
	Text deleteDialog;

	@DialogField( fieldLabel="'Navigate away Dialog' text", fieldDescription="Default value is - 'Closing the Nutrition Calculator will delete all of your items. Are you sure you want to leave?'", tab = 4)
	@DialogFieldSet(namePrefix = "exitDialog/" , title = "'Navigate away Dialog' text" )
	@ChildResource(name = "exitDialog")
	@Inject @Default(values="Closing the Nutrition Calculator will delete all of your items. Are you sure you want to leave?")
	Text exitDialog;

	@DialogField(fieldLabel="Maximium items that can be added in NC", fieldDescription="Default value is 20", tab = 4)
	@NumberField
	@Inject @Default(intValues = 20)
	int maxCount;

	@DialogField( fieldLabel="'Items limit reached. Remove some items to add new ones' text", fieldDescription="Default value is - 'Delete some items to add more items'", tab = 4)
	@DialogFieldSet(namePrefix = "limitDialog/" , title = "'Items limit'" )
	@ChildResource(name = "limitDialog")
	@Inject @Default(values="Delete some items to add more items")
	Text limitDialog;

	@DialogField(fieldLabel="'Limit dialog OK button' text" , tab = 4)
	@TextField
	@Inject @Default(values="OK")
	String okButton;

	@DialogField(fieldLabel="Allergen 'Contains' text" , tab = 4)
	@TextField
	@Inject @Default(values="Contains:")
	String allergen;

	@DialogField(fieldDescription="Default value is 'May Contain:'", fieldLabel="Allergen 'May Contains' Text" , tab = 4)
	@TextField
	@Inject @Default(values="May Contain:")
	String allergenMayText;

	@DialogField(fieldLabel="Legal footer collapsed button text", fieldDescription = "The text that gets " +
			"displayed on the button when legal footer is collapsed. Default value is 'More'", tab = 4)
	@TextField
	@Inject @Default(values="More")
	String legalMore;

	@DialogField(fieldLabel="Legal footer expanded button text", fieldDescription = "The text that gets " +
			"displayed on the button when legal footer is expaded. Default value is 'Less'", tab = 4)
	@TextField
	@Inject @Default(values="Less")
	String legalLess;

	@DialogField(fieldLabel="'select' text" , tab = 5)
	@TextField
	@Inject @Default(values="select")
	String adaSelect;

	@DialogField(fieldLabel="Legal note title text" , tab = 5)
	@TextField
	@Inject @Default(values="legal note")
	String adaLegalTitle;

	@DialogField(fieldLabel="Serving Size text" , tab = 5)
	@TextField
	@Inject @Default(values="Select Serving Size for")
	String adaServingSize;

	@DialogField(fieldLabel="Customize Ingredients text" , tab = 5)
	@TextField
	@Inject @Default(values="Customize Ingredients")
	String adaCustomizeIngredients;

	@DialogField(fieldLabel="Meal Title" , tab = 5)
	@TextField
	@Inject @Default(values="my meal details")
	String adaMealTitle;

	@DialogField(fieldLabel="Category Title" , tab = 5)
	@TextField
	@Inject @Default(values="menu categories")
	String adaCategoryTitle;

	@DialogField(fieldLabel="Item Title" , tab = 5)
	@TextField
	@Inject @Default(values="menu items")
	String adaItemTitle;

	@DialogField(fieldLabel="Item text" , tab = 5)
	@TextField
	@Inject @Default(values="item")
	String adaItem;

	@Inject
	SlingHttpServletRequest request;

	@Inject
	SlingScriptHelper slingScriptHelper;

	@Inject
	Page currentPage;

	@OSGiService
	McdWebServicesConfig mcdWebServicesConfig;

	@OSGiService
	McdFactoryConfigConsumer mcdFactoryConfigConsumer;

	@OSGiService
	NutrientProvider nutrientProvider;




	private String selectCategoryText1;

	private String selectCategoryText2;

	private String dnaCategoryDetailsUrl;

	private String dnaItemDetailsUrl;

	private String dnaItemListUrl;

	private List<CategoryDetail> categoryDetailList = new ArrayList<CategoryDetail>();
		
	private McdFactoryConfig mcdFactoryConfig;


	private String serves;
	
	private String showLiveDataValue;
	
	private String flagCheck = "[\"true\"]";
	
	private String unit_flag;

	private String hyperlink = "";

	private boolean isNutritionInfoAtProductLevel = true;

	private List<SecondaryNutrientListPOJO> secondaryNutrientListContainer = new ArrayList<>();

	private List<SecondaryNutrientListPOJO> secondaryNutrientListMobileContainer = new ArrayList<>();
	

	/**
	 * Method to perform Post Initialization Tasks.
	 */
	@PostConstruct
	public final void activate() {
		if(null != title) {
			selectCategoryText1 = "";
			showLiveDataValue = request.getParameter("showLiveData");
			if (StringUtils.isBlank(showLiveDataValue)) {
				showLiveDataValue = String.valueOf(true);
			}

			if (selectCategoryText != null && selectCategoryText.getText() != null
					&& selectCategoryText.getText().contains("##")) {
				String[] arr = selectCategoryText.getText().split("##");
				selectCategoryText2 = selectCategoryText.getText();
				selectCategoryText2 = arr[0];
				if (arr.length > 1) {
					selectCategoryText1 = arr[1];
				}
			}


			if (healthHyperlink != null && healthHyperlink.getText() != null) {
				tagRemoval(healthHyperlink.getText());
			}
			LOGGER.error("Generated Hyperlink thorugh RichText id: " + hyperlink);
			LOGGER.error("Optional Allergence Contains: " + allergenMayText);
			LOGGER.error("Nutrition Column Heading" + nutritionColumnHeading);
			LOGGER.error("Measurement Unit Column Heading" + measurementUnitColumnHeading);
			LOGGER.error("Percentage Unit Column Heading" + percentageUnitColumnHeading);
			LOGGER.error("Close Item Details Text" + closeItemDetails);
			LOGGER.error("Explore Item Details Text" + exploreItemDetails);

			populateMcdConfig();
			populateCategoryDetailsUrl();
			populateItemDetailsUrl();
			populateItemListUrl();
			populateCategory();
			populateContainers();
			populateMarketingNames();
		}

	}

	private void populateContainers() {
		if (secondaryNutrientList != null) {
			secondaryNutrientListContainer.addAll(secondaryNutrientList);
		}
		if (secondaryNutrientList2 != null) {
			secondaryNutrientListContainer.addAll(secondaryNutrientList2);
		}
		if (secondaryNutrientList3 != null) {
			secondaryNutrientListContainer.addAll(secondaryNutrientList3);
		}
		if (secondaryNutrientListMobile != null) {
			secondaryNutrientListMobileContainer.addAll(secondaryNutrientListMobile);
		}
		if (secondaryNutrientListMobile2 != null) {
			secondaryNutrientListMobileContainer.addAll(secondaryNutrientListMobile2);
		}
	}

	private void populateMarketingNames() {

		final Resource resource = request.getResource();
		final Map<String, String> namesById = nutrientProvider.getNutrientIdMapping(resource, null);

		for (NutritionListPOJO nutrient : nutrientList) {
			if (nutrient.getMarketingName() == null) {
				nutrient.setMarketingName(namesById.get(nutrient.getId()));
			}
		}

		for (List<SecondaryNutrientListPOJO> list:
				Arrays.asList(secondaryNutrientList, secondaryNutrientList2, secondaryNutrientList3,
						secondaryNutrientListMobile, secondaryNutrientListMobile2)) {
			for (SecondaryNutrientListPOJO nutrient : list) {
				if (nutrient.getMarketingName() == null) {
					nutrient.setMarketingName(namesById.get(nutrient.getId()));
				}
			}
		}
	}

	private void populateCategory() {
		LOGGER.debug("Inside populate Category to populate category list..");
		if (categoryList != null && !categoryList.isEmpty()) {
			for (CategoryListPOJO categoryListPOJO : categoryList) {
				CategoryDetail categoryDetail = new CategoryDetail();
				String[] parts = categoryListPOJO.getCategoryList().split("_");
				categoryDetail.setId(parts[0]);
				final String name = parts.length == 2 ? parts[1] : "";
				categoryDetail.setName(name);
				final String marketingName = categoryListPOJO.getCategoryMarketingName();
				if (marketingName != null && !"".equals(marketingName)) {
					categoryDetail.setMarketingName(marketingName);
				} else {
					categoryDetail.setMarketingName(name);
				}
				final String imagePath = categoryListPOJO.getCategoryImagePath();
				if (imagePath != null && !"".equals(imagePath)) {
					categoryDetail.setImagePath(imagePath);
				}
				else {
					categoryDetail.setImagePath(componentImageFolderPath+"/default.png");
				}

				categoryDetailList.add(categoryDetail);
			}
		} else {
			LOGGER.error("CategoryList is empty..");
		}
	}
	
	private void populateMcdConfig() {
		String country = PageUtil.getCountry(currentPage);
		String language = PageUtil.getLanguage(currentPage);

		LOGGER.debug(
				" In Nutrition Calculator  Resource Country Code is" + country + "Resource Language Code is"
						+ language);
		if (country != null && language != null) {
			McdFactoryConfigConsumer mcdFactoryConfigConsumer = slingScriptHelper.getService(McdFactoryConfigConsumer.class);
			if (mcdFactoryConfigConsumer != null && mcdWebServicesConfig != null) {
				mcdFactoryConfig = mcdFactoryConfigConsumer.getMcdFactoryConfig(country, language);
			}
		}
	}

	private void populateCategoryDetailsUrl() {
		StringBuilder url = new StringBuilder();

		if (mcdFactoryConfig != null) {
			
			isNutritionInfoAtProductLevel = mcdFactoryConfig.isNutritionInfoAtProductLevel();


			url.append(null!=mcdWebServicesConfig? mcdWebServicesConfig.getCategoryDetailUrl() : null)
					.append(ApplicationConstants.URL_QS_START).append(ApplicationConstants.PN_COUNTRY)
					.append(ApplicationConstants.PN_EQUALS).append(mcdFactoryConfig.getDnaCountryCode())
					.append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
					.append(ApplicationConstants.PN_LANGUAGE).append(ApplicationConstants.PN_EQUALS)
					.append(mcdFactoryConfig.getDnaLanguageCode())
					.append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
					.append(ApplicationConstants.PN_SHOW_LIVE_DATA).append(ApplicationConstants.PN_EQUALS)
					.append(showLiveDataValue).append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
					.append(ApplicationConstants.PN_COPS_FILTER).append(ApplicationConstants.PN_EQUALS)
					.append(true).append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
					.append(ApplicationConstants.PN_SHOW_NATIONAL_COOP).append(ApplicationConstants.PN_EQUALS)
					.append(true);
			dnaCategoryDetailsUrl = url.toString();
			LOGGER.debug("Cat details url ---- " + url.toString());

		}

	}

	private void populateItemDetailsUrl() {
		StringBuilder url = new StringBuilder();
		if (mcdFactoryConfig != null) {
			url.append(null!=mcdWebServicesConfig ? mcdWebServicesConfig.getItemDetailUrl() : null)
					.append(ApplicationConstants.URL_QS_START).append(ApplicationConstants.PN_COUNTRY)
					.append(ApplicationConstants.PN_EQUALS).append(mcdFactoryConfig.getDnaCountryCode())
					.append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
					.append(ApplicationConstants.PN_LANGUAGE).append(ApplicationConstants.PN_EQUALS)
					.append(mcdFactoryConfig.getDnaLanguageCode())
					.append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
					.append(ApplicationConstants.PN_SHOW_LIVE_DATA).append(ApplicationConstants.PN_EQUALS)
					.append(showLiveDataValue);
			dnaItemDetailsUrl = url.toString();
			LOGGER.debug("Item details url ---- " + url.toString());

		}
	}

	private void populateItemListUrl() {
		StringBuilder url = new StringBuilder();
		if (mcdFactoryConfig != null) {
			url.append(null!=mcdWebServicesConfig ? mcdWebServicesConfig.getItemListUrl() : null)
					.append(ApplicationConstants.URL_QS_START).append(ApplicationConstants.PN_COUNTRY)
					.append(ApplicationConstants.PN_EQUALS).append(mcdFactoryConfig.getDnaCountryCode())
					.append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
					.append(ApplicationConstants.PN_LANGUAGE).append(ApplicationConstants.PN_EQUALS)
					.append(mcdFactoryConfig.getDnaLanguageCode())
					.append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
					.append(ApplicationConstants.PN_SHOW_LIVE_DATA).append(ApplicationConstants.PN_EQUALS)
					.append(showLiveDataValue).append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
					.append(ApplicationConstants.NUTRIENT_REQ).append(ApplicationConstants.PN_EQUALS)
					.append(ApplicationConstants.PN_YES);
			dnaItemListUrl = url.toString();
			LOGGER.debug("Item List url ---- " + url.toString());
		}
	}
	
	private void tagRemoval(String oldHyperlink) {
		if(null!=oldHyperlink) {
			String[] hyper = oldHyperlink.split("");
			for (int counter = 0; counter < hyper.length; counter++) {
				if (counter > 2 && (hyper.length - (counter + 4)) > 1) {
					hyperlink = hyperlink + hyper[counter];
				}
			}
		}
	}
	
	public String getAllergenMayText() {
		return allergenMayText;
	}

	public String getHyperlink() {
		return hyperlink;
	}

	public void setHyperlink(String hyperlink) {
		this.hyperlink = hyperlink;
	}

	public String getNutritionColumnHeading() {
		return nutritionColumnHeading;
	}

	public void setNutritionColumnHeading(String nutritionColumnHeading) {
		this.nutritionColumnHeading = nutritionColumnHeading;
	}

	public String getMeasurementUnitColumnHeading() {
		return measurementUnitColumnHeading;
	}

	public void setMeasurementUnitColumnHeading(String measurementUnitColumnHeading) {
		this.measurementUnitColumnHeading = measurementUnitColumnHeading;
	}

	public String getPercentageUnitColumnHeading() {
		return percentageUnitColumnHeading;
	}

	public void setPercentageUnitColumnHeading(String percentageUnitColumnHeading) {
		this.percentageUnitColumnHeading = percentageUnitColumnHeading;
	}


	public String getAdaCustomizeIngredients() {
		return adaCustomizeIngredients;
	}
	
	public String getAdaItem() {
		return adaItem;
	}
	
	public String getAdaItemTitle() {
		return adaItemTitle;
	}

	public String getAdaCategoryTitle() {
		return adaCategoryTitle;
	}

	public String getAdaMealTitle() {
		return adaMealTitle;
	}
	
	public String getAdaSelect() {
		return adaSelect;
	}

	public String getAdaLegalTitle() {
		return adaLegalTitle;
	}

	public String getAdaServingSize() {
		return adaServingSize;
	}
	
	public String getAllergen() {
		return allergen;
	}
	
	public String getSelectItem() {
		return selectItem;
	}
	
	public String getDeleteButton() {
		return deleteButton;
	}

	public String getDvText() {
		return dvText;
	}

	public String getBackButton() {
		return backButton;
	}
	
	public String getClickHere() {
		return clickHere;
	}

	public String getShowText() {
		return showText;
	}

	public String getLegalMore() {
		return legalMore;
	}

	public String getHideText() {
		return hideText;
	}

	public String getVideoId() {
		return videoId;
	}

	public String getCloseIngredients() {
		return closeIngredients;
	}

	public String getAddCalculator() {
		return addCalculator;
	}

	public String getCustomizeIngredients() {
		return customizeIngredients;
	}

	public String getAddMore() {
		return addMore;
	}
	
	public String getSelectCategoryText() {
		return selectCategoryText2;
	}

	public String getNoButton() {
		return noButton;
	}
	
	public String getSelectCategoryText1() {
		return selectCategoryText1;
	}

	public String getAddButton() {
		return addButton;
	}

	public String getViewButton() {
		return viewButton;
	}

	public String getDvDisclaimer() {
		return dvDisclaimer;
	}

	public String getDeleteAllButton() {
		return deleteAllButton;
	}

	public String getYesButton() {
		return yesButton;
	}

	public String getDeleteAllDialog() {
		return deleteAllDialog.getText();
	}

	public String getDeleteDialog() {
		return deleteDialog.getText();
	}

	public String getServes() {
		return serves;
	}

	public String getItemIn() {
		return itemIn;
	}

	public String getMyMeal() {
		return myMeal;
	}
	
	public String getLegalLess() {
		return legalLess;
	}
	
	public String getLimitDialog() {
		return limitDialog.getText();
	}

	public int getMaxCount() {
		return maxCount;
	}

	public String getOkButton() {
		return okButton;
	}
	
	public List<SecondaryNutrientListPOJO> getSecondaryNutrientsList() {
		return secondaryNutrientList;
	}
	
	public List<SecondaryNutrientListPOJO> getSecondaryNutrientsList2() {
		return secondaryNutrientList2;
	}
	
	public List<SecondaryNutrientListPOJO> getSecondaryNutrientsList3() {
		return secondaryNutrientList3;
	}
	
	public List<NutritionListPOJO> getNutrientsList() {
		return nutrientList;
	}

	public List<CategoryDetail> getCategoryDetailList() {
		return categoryDetailList;
	}
		
	public String getExitDialog() {
		return exitDialog.getText();
	}

	public String getTitle() {
		return title;
	}

	public String getDnaCategoryDetailsUrl() {
		return dnaCategoryDetailsUrl;
	}

	public String getDnaItemDetailsUrl() {
		return dnaItemDetailsUrl;
	}

	public String getDnaItemListUrl() {
		return dnaItemListUrl;
	}

	public String getItemImageFolderPath() {
		return itemImageFolderPath;
	}

	public String getComponentImageFolderPath() {
		return componentImageFolderPath;
	}
	
	public String getSecondaryColCountMobile() {
		return secondaryColCountMobile;
	}
	
	public String getSecondaryColCount() {
		return secondaryColCount;
	}
	
	public List<SecondaryNutrientListPOJO> getSecondaryNutrientsListMobile2() {
		return secondaryNutrientListMobile2;
	}

	public List<SecondaryNutrientListPOJO> getSecondaryNutrientsListMobile() {
		return secondaryNutrientListMobile;
	}
	
	public List<SecondaryNutrientListPOJO> getSecondaryNutrientsListContainer() {
		return secondaryNutrientListContainer;
	}

	public void setSecondaryNutrientsListContainer(
			List<SecondaryNutrientListPOJO> secondaryNutrientsListContainer) {
		this.secondaryNutrientListContainer = secondaryNutrientsListContainer;
	}

	public List<SecondaryNutrientListPOJO> getSecondaryNutrientsListMobileContainer() {
		return secondaryNutrientListMobileContainer;
	}

	public void setSecondaryNutrientsListMobileContainer(List<SecondaryNutrientListPOJO> secondaryNutrientsListMobileContainer) {
		this.secondaryNutrientListMobileContainer = secondaryNutrientsListMobileContainer;
	}
	
	public String getCloseItemDetails() {
		return closeItemDetails;
	}

	public String getExploreItemDetails() {
		return exploreItemDetails;
	}
	
	public String getSecondaryNutrientsViewType() {
		return secondaryNutrientsViewType;
	}
	
	public boolean isNutritionInfoAtProductLevel() {
		return isNutritionInfoAtProductLevel;
	}
}

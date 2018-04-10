<%--
  Changes done only on line no. 174
  ADOBE CONFIDENTIAL

  Copyright 2015 Adobe Systems Incorporated
  All Rights Reserved.

  NOTICE:  All information contained herein is, and remains
  the property of Adobe Systems Incorporated and its suppliers,
  if any.  The intellectual and technical concepts contained
  herein are proprietary to Adobe Systems Incorporated and its
  suppliers and may be covered by U.S. and Foreign Patents,
  patents in process, and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Adobe Systems Incorporated.
--%><%
%><%@include file="/libs/granite/ui/global.jsp"%><%
%><%@page session="false"
          import="org.apache.commons.lang.StringUtils,
                  org.joda.time.DateTime,
                  org.joda.time.format.DateTimeFormat,
                  org.joda.time.format.DateTimeFormatter,
                  com.adobe.granite.ui.components.AttrBuilder,
                  com.adobe.granite.ui.components.Config,
                  com.adobe.granite.ui.components.Field,
                  com.adobe.granite.ui.components.Tag,
                  com.day.cq.i18n.I18n"%><%--###
DatePicker
==========

.. granite:servercomponent:: /libs/granite/ui/components/coral/foundation/form/datepicker
   :supertype: /libs/granite/ui/components/coral/foundation/form/field

   A field that allows user to enter date.

   It extends :granite:servercomponent:`Field </libs/granite/ui/components/coral/foundation/form/field>` component.

   It has the following content structure:

   .. gnd:gnd::

      [granite:FormDatePicker] > granite:FormField

      /**
       * The name that identifies the field when submitting the form.
       * The `SlingPostServlet @TypeHint <http://sling.apache.org/documentation/bundles/manipulating-content-the-slingpostservlet-servlets-post.html#typehint>`_ hidden input with value ``Date`` is also generated based on the name.
       */
      - name (String)

      /**
       * The value of the field.
       */
      - value (StringEL)

      /**
       * A hint to the user of what can be entered in the field.
       */
      - emptyText (String) i18n

      /**
       * Indicates if the field is in disabled state.
       */
      - disabled (Boolean)

      /**
       * Indicates if the field is mandatory to be filled.
       */
      - required (Boolean)

      /**
       * The name of the validator to be applied. E.g. ``foundation.jcr.name``.
       * See :doc:`validation </jcr_root/libs/granite/ui/components/coral/foundation/clientlibs/foundation/js/validation/index>` in Granite UI.
       */
      - validation (String) multiple

      /**
       * The type of the picker.
       */
      - type (String) = 'date' < 'date', 'datetime', 'time'

      /**
       * The date format for display.
       */
      - displayedFormat (String) i18n

      /**
       * The date format of the actual value, and for form submission.
       */
      - valueFormat = 'YYYY-MM-DD[T]HH:mm:ss.000Z'

      /**
       * The minimum boundary of the date.
       */
      - minDate (String)

      /**
       * The maximum boundary of the date.
       */
      - maxDate (String)

      /**
       * Indicates if a informative message should be displayed regarding timezone prevalence
       */
      - displayTimezoneMessage (Boolean)

      /**
       * Specifies a CSS selector targeting another datepickers that are before this datepicker.
       * If those datepickers are not before this datepicker, it will be invalid.
       */
      - beforeSelector (String)

      /**
       * Specifies a CSS selector targeting another datepickers that are after this datepicker.
       * If those datepickers are not after this datepicker, it will be invalid.
       */
      - afterSelector (String)
###--%><%

    Config cfg = cmp.getConfig();
    ValueMap vm = (ValueMap) request.getAttribute(Field.class.getName());
    Field field = new Field(cfg);

    boolean isMixed = field.isMixed(cmp.getValue());

    String name = cfg.get("name", String.class);

    Tag tag = cmp.consumeTag();
    AttrBuilder attrs = tag.getAttrs();
    cmp.populateCommonAttrs(attrs);

    if (isMixed) {
        attrs.addClass("foundation-field-mixed");
    }

    attrs.add("type", cfg.get("type", String.class));
    attrs.add("name", name);
    attrs.add("min", cfg.get("minDate", String.class));
    attrs.add("max", cfg.get("maxDate", String.class));
    attrs.addDisabled(cfg.get("disabled", false));
    attrs.add("displayformat", i18n.getVar(cfg.get("displayedFormat", String.class)));
    attrs.add("valueformat", cfg.get("valueFormat", "YYYY-MM-DD[T]HH:mm:ss.000Z"));
    attrs.add("headerformat", i18n.get("MMMM YYYY", "Datepicker headline, see moment.js for allowed formats"));

    String beforeSelector = cfg.get("beforeSelector");
    if (!StringUtils.isEmpty(beforeSelector)) {
        attrs.add("data-granite-datepicker-before", beforeSelector);
    }

    String afterSelector = cfg.get("afterSelector");
    if (!StringUtils.isEmpty(afterSelector)) {
        attrs.add("data-granite-datepicker-after", afterSelector);
    }

    if (isMixed) {
        attrs.add("placeholder", i18n.get("<Mixed Entries>")); // TODO Maybe define this String somewhere
    } else {
        attrs.add("value", vm.get("value", String.class));
        attrs.add("placeholder", i18n.getVar(cfg.get("emptyText", String.class)));
    }

    attrs.addBoolean("required", cfg.get("required", false));

    String validation = StringUtils.join(cfg.get("validation", new String[0]), " ");
    attrs.add("data-foundation-validation", validation);
    attrs.add("data-validation", validation); // Compatibility

%><coral-datepicker <%= attrs.build() %>></coral-datepicker><%

    if (!StringUtils.isBlank(name)) {
        AttrBuilder typeAttrs = new AttrBuilder(request, xssAPI);
        typeAttrs.addClass("foundation-field-related");
        typeAttrs.add("type", "hidden");
        typeAttrs.add("value", cfg.get("typeHint", "Date")); //Changes done by ICF(Sandeep), changed this configurable to handle time field in feature callout component
        typeAttrs.add("name", name + "@TypeHint");

        %><input <%= typeAttrs.build() %>><%
    }

    if (cfg.get("displayTimezoneMessage", false)) {
        DateTime now = new DateTime();
        long offsetMin = now.getZone().getOffset(now.getMillis()) / (1000 * 60);
        String serverOffset = DateTimeFormat.forPattern("ZZ").withLocale(request.getLocale()).print(now);

        %><p class="granite-datepicker-timezone granite-coral-Form-fieldlongdesc" data-granite-datepicker-timezone-server="<%= offsetMin %>">
            <coral-icon icon="alert" size="XS"></coral-icon>
            <%= xssAPI.filterHTML(i18n.get("<span>Your timezone (UTC<span class='granite-datepicker-timezone-client'></span>) will be used instead of the server setting (UTC{0})</span>", null, serverOffset)) %>
        </p><%
    }
%>
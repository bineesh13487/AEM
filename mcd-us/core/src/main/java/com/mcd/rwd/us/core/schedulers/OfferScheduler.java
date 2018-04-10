package com.mcd.rwd.us.core.schedulers;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationStatus;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.*;
import org.apache.felix.scr.annotations.Properties;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.settings.SlingSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by deepti_b on 1/19/2017.
 */

@Service
@Component(name="MCD US-Offer Exception List Scheduler", label="MCD US-Offer Exception List Scheduler", description="Schedule job which regularly deletes offers that are expired.",
        metatype=true,immediate = true)
@Properties({
        @Property(label="Scheduler Running Interval",description="Scheduler Running Interval(Quartz Cron Expression)",name="scheduler.expression", value="0 0/15 * 1/1 * ? *"),
        @Property(
                label="ExceptionList Scheduler concurrent executions",
                description="Allow concurrent executions in ExceptionList Scheduler",
                name="scheduler.concurrent",
                boolValue=false,
                propertyPrivate=true
        )
})
public class OfferScheduler implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(OfferScheduler.class);

    private Session session;

    //Inject a Sling ResourceResolverFactory
    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private Replicator replicator;

    @Reference
    private SlingSettingsService settingsService;

    @Reference
    private McdWebServicesConfig mcdWebServicesConfig;


    private long lastModified,lastPublished=0;

    private boolean doActivate=false;

    private boolean runScheduler=false;


    public void run() {
        Set<String> runmodes = settingsService.getRunModes();
        for (String runmode : runmodes) {
            if (runmode.equals("author")) {
                runScheduler = true;
                break;
            }
        }
        if (runScheduler) {
            logger.info("------------------------------In offers scheduler run method-------------------------------------");
            Map<String, Object> param = new HashMap<String, Object>();
            param.put(ResourceResolverFactory.SUBSERVICE, "offersservice");
            ResourceResolver resolver = null;
            try {
                resolver = resolverFactory.getServiceResourceResolver(param);
                session = resolver.adaptTo(Session.class);
                PageManager pageManager = resolver.adaptTo(PageManager.class);
                String[] offerPages=mcdWebServicesConfig.getOfferPages();
                for (String path : offerPages) {
                   // Page offerpage = pageManager.getPage("/content/us/en-us/config/test_offers");
                   // Resource offerRes = resolver.getResource("/content/us/en-us/config/test_offers");
                    Page offerpage = pageManager.getPage(path);
                    Resource offerRes = resolver.getResource(path);
                    if (offerpage != null) {
                        lastModified = offerpage.getLastModified() == null ? -1 : offerpage
                                .getLastModified().getTimeInMillis();
                    }
                    ReplicationStatus rs = offerRes.adaptTo(ReplicationStatus.class);
                    if (rs != null && rs.getLastPublished() != null) {
                        lastPublished = rs.getLastPublished().getTimeInMillis();
                    }
                    if (lastModified > lastPublished) {
                        doActivate = false;
                    } else {
                        doActivate = true;
                    }
                    if (null != offerpage && null != offerpage.getContentResource("offerslist")) {
                        Iterator<Resource> childResource = offerpage.getContentResource("offerslist").listChildren();
                        while (childResource.hasNext()) {
                            Resource offerResource = childResource.next();
                            if (offerResource.isResourceType("mcd-us/components/content/offerexceptionlist")) {
                                ValueMap offersValueMap = offerResource.getValueMap();
                                String offerExpiryStrng = offersValueMap.get("offerExpiryDate", "");
                               if(!StringUtils.isEmpty(offerExpiryStrng)){
                                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                                    Date offerExpiryDate = formatter.parse(offerExpiryStrng);
                                    Date todayDate = new Date();
                                    if (getZeroTimeDate(offerExpiryDate).compareTo(getZeroTimeDate(todayDate)) < 0) {
                                        resolver.delete(offerResource);
                                    }
                               }

                            }
                        }
                        resolver.commit();
                        if (doActivate) {
                            replicator.replicate(session, ReplicationActionType.ACTIVATE, offerRes.getPath());
                        }
                    }
                }
            } catch (Exception e) {
                logger.info("------------------------------Errpr obtained while creating session-------------------------------------"+e.getMessage());
            }
            if (resolver != null) {
                resolver.close();
            }
        }

    }

    private  Date getZeroTimeDate(Date fecha) {
        Date res = fecha;
        Calendar calendar = Calendar.getInstance();

        calendar.setTime( fecha );
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        res = calendar.getTime();

        return res;
    }
}

package com.mcd.rwd.us.core.service;

import org.apache.felix.scr.annotations.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created  by srishma yarra.
 */
@Component(label = "Mcd Factory Configuration Consumer", description = "Consumer", immediate = true,
		metatype = true)
@Service(McdFactoryConfigConsumer.class)
public class McdFactoryConfigConsumer {

	@Reference(referenceInterface = McdFactoryConfig.class,
			cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	private List<McdFactoryConfig> mcdFactoryConfigs;

	protected synchronized void bindMcdFactoryConfig(final McdFactoryConfig config) {
		if (mcdFactoryConfigs == null) {
			mcdFactoryConfigs = new ArrayList();
		}
		mcdFactoryConfigs.add(config);
	}

	protected synchronized void unbindMcdFactoryConfig(final McdFactoryConfig config) {
		mcdFactoryConfigs.remove(config);
	}

	public synchronized List<McdFactoryConfig> getMcdFactoryConfig() {
		return mcdFactoryConfigs;
	}

	public synchronized McdFactoryConfig getMcdFactoryConfig(String country, String language) {
		if (mcdFactoryConfigs != null) {
			for (int i = 0; i < mcdFactoryConfigs.size(); i++) {
				McdFactoryConfig mcdFactoryConfig = mcdFactoryConfigs.get(i);
				boolean isLanguageNodeNotAvailable = mcdFactoryConfig.isLanguageNodeNotAvailable();
				if (isLanguageNodeNotAvailable) {
					if (mcdFactoryConfig.getCountry().equals(country)) {
						return mcdFactoryConfig;
					}
				} else if (mcdFactoryConfig.getCountry().equals(country) && mcdFactoryConfig.getLanguage()
							.equals(language)) {
					return mcdFactoryConfig;
				}
			}
		}
		return null;
	}
}


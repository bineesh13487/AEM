package com.mcd.akamai.core.externalize;

import java.util.Set;

public interface AkamaiExternalizer {

    Set<String> mapPath(final String internal);

}

package com.alc4obiosio.travelmantics;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by Mbuodile Obiosio on Aug 02,2019
 * https://twitter.com/cazewonder
 * Nigeria.
 */
@GlideModule
public class MyGlideApp extends AppGlideModule {

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}

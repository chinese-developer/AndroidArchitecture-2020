/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.android.cache;

import io.reactivex.annotations.Nullable;

public interface Encipher {

    String encrypt(@Nullable String origin);

    String decrypt(@Nullable String encrypted);

}

package com.app.base.utils.domain;

import android.annotation.SuppressLint;

import com.android.base.TagsFactory;
import com.android.cache.Storage;
import com.android.cache.TypeFlag;
import com.app.base.AppContext;
import com.app.base.common.Keys;
import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;

import io.reactivex.Observable;
import timber.log.Timber;

/**
 * 获取最快的域名
 * <p>
 * 基础逻辑如下：
 * <p>
 * 1、读取本地域名列表（可能是上一次请求回来的，也可能是写死在本地的）。
 * <p>
 * 2、这些域名，同时向服务器请求一个接口，哪个域名返回的最快，就认为哪个域名是最快域名。
 * <p>
 * 3、请求的接口，会返回一个域名列表，这个列表，是现在可用的域名列表，下一次做最快域名选择的时候就是基于这个列表。
 */
public class DomainUtils {
    private static final String KEY_FAST_DOMAIN = "key_fast_domain";
    private static DomainUtils instance;

    /**
     * 0:正在选择中，1：成功获取一个可用域名，2：域名选择失败
     */
    public int selectStatus = 0;

    /**
     * 成功得到的最快域名，域名列表
     */
    private DomainResponse domainResponse;

    private DomainUtils() {

    }

    public static DomainUtils getInstance() {
        if (instance == null) {
            instance = new DomainUtils();
        }
        return instance;
    }

    /**
     * 1、just：产生一个事件流
     * <p>
     * 2、map：在事件流发射的时候，去获取已有的domain列表
     * <p>
     * 3、flatMap：在获取domain列表以后，在下游转换成新的ObservableSource<DomainModel>>，再发射出去，让下游拿到DomainModel对象
     * <p>
     * 4、flatMap：在拿到DomainModel对象以后，做网络请求，继续向下游发射HttpModel<List<DomainModel>>  （HttpModel后续可能是一个通用对象，包含了请求内容和响应内容）
     * <p>
     * 5、最先拿到的HttpModel<List<DomainModel>>，认为是最快的域名，做相应的保存工作
     * <p>
     * 6、doFinally是所有数据流结束以后执行的地方，在doFinally中做最后的检查
     * <p>
     * 7、为了保证所有事件流执行完，采用了：onExceptionResumeNext(Observable.empty())
     */
    @SuppressLint("CheckResult")
    public void getFastDomain(ArrayList<String> domainList) {
        try {
            Storage storage = AppContext.get().storageManager.stableStorage();

            if (domainList == null || domainList.size() == 0) {
                domainList = storage.getEntity(Keys.KEY_DOMAIN_LIST, new TypeFlag<ArrayList<String>>() {}.getType());
            }

            Observable.fromIterable(domainList).flatMap((String domain) -> AppContext.get().appDataSource.fetchDomain(domain)
                    .onErrorResumeNext(Observable.empty()))
                    .doFinally(() -> {
                        if (domainResponse == null) {
                            selectStatus = 2;
                            Timber.tag(TagsFactory.debug).d("域名选择失败");
                        }
                    }).subscribe(response -> {
                if (domainResponse == null) {
                    if (response.fastDomain != null) {
                        storage.putEntity(Keys.KEY_FAST_DOMAIN, response.fastDomain);
                    }
                    Timber.tag(TagsFactory.debug).d("域名选择成功%s", response.fastDomain);
                    domainResponse = response;
                    selectStatus = 1;
                }
            }, throwable -> ToastUtils.showShort(throwable.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.app.base.router;

import android.content.Intent;

/**
 * 注意：报错 There is no route match the path [/xxx/xxx], in group [xxx][ ] 尝试卸载应用再次安装。2
 */
public class RouterPath {

    public static final String ACTION_KEY = "action_key"; //int

    /**
     * 启动页
     */
    public static final class Launcher {
        public static final String PATH = "/arch/launcher";
    }

    /**
     * 全局
     */
    public static final class Global {
        public static final String PATH = "/arch_global/common";
        public static final String SERVICE = "/arch_global/service";

        public static final int REQUEST_CODE_COMMON = 0x123;
    }

    /**
     * 首页
     */
    public static final class Main {
        public static final String PATH = "/arch_home/main";

        public static final String MEDIA_PLAYER_PATH = "/arch_home/media_player";

        public static final String PAGE_KEY = "page_key";
        public static final int HOME = 0;
        public static final int DIARY = 1;
        public static final int MINE = 2;
        public static final int WRITE_DIARY = 3;
        public static final int SETTING = 4;

        /**
         * 登录过期后，需要重新登录，统一先回到主界面，然后由主界面发起登录流程
         */
        public static final int ACTION_RE_LOGIN = 1;
    }

    /**
     * 登录注册相关
     */
    public static final class Account {
        public static final String PATH = "/arch_account/account";
        public static final int REQUEST_CODE = 1003;
        /**
         * 用于从 {@link android.app.Activity#startActivityForResult(Intent, int)}方式中获取登录类型，参考{@link #LOGIN_BY_REGISTER}和{@link #LOGIN_BY_OTHER}
         */
        public static final String LOGIN_TYPE = "LOGIN_TYPE_KEY";
        public static final int LOGIN_BY_REGISTER = 2;//注册的方式登录
        public static final int LOGIN_BY_OTHER = 1;//其他方式登录
    }

    /**
     * 内置浏览器
     */
    @SuppressWarnings("unused")
    public static final class Browser {
        public static final String PATH = "/arch_browser/browser";

        public static final String URL_KEY = "url_key";//string
        public static final String SHOW_HEADER = "show_header";//boolean
        public static final String FRAGMENT_KEY = "fragment_class_key";//string

        public static final String GROW_GUIDE = "grow_up.html#/growGuide";
        public static final String GROW_UP = "grow_up.html#/";

        public static final String DIARY_EXAMPLE = "diary_example.html";

        public static final String TIME_RANK = "leaderboard.html#/ranktime";
        public static final String STEPS_RANK = "leaderboard.html#/ranksport";
        public static final String APP_RANK = "leaderboard.html#/ranktype";
        public static final String GUARD_REPORT = "guard_report.html";
        public static final String PEER_APP_RANK = "leaderboard.html#/ranksoft";

        public static final String ABOUT_US = "about_me.html#/";
        public static final String HELP_FEEDBACK = "help.html";
    }

    /**
     * x5 webview
     */
    public static final class X5WebView {
        public static final String PATH = "/arch_x5/webview";

        public static final String URL = "url";//string
        public static final String TITLE = "title";//string
        public static final String isLoadData = "isLoadData";//boolean
        public static final String ACTION_AUTO = "javascript:";//string
    }

}

package com.android.base.utils.binding.adapter

import com.android.base.utils.ktx.yes

class BindingListener {

    private var execute: (() -> Unit)? = {}

    private var checkAction: (() -> Boolean)? = { true }

    constructor(execute: () -> Unit) {
        this.execute = execute
    }

    /**
     * @param execute 命令绑定
     * @param checkAction 检查判断是否需要执行 默认执行
     */
    constructor(execute: () -> Unit, checkAction: () -> Boolean = { false }) {
        this.execute = execute
        this.checkAction = checkAction
    }

    /** 不带参数执行 */
    fun executeAction() {
        if (checkAction == null) {
            execute?.invoke()
        } else {
            checkAction?.let {
                it().yes {
                    execute?.invoke()
                }
            }
        }
    }
}
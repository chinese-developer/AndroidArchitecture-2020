package com.android.base.utils.binding.adapter

import com.android.base.utils.ktx.yes

class BindingListenerWithParams<T> {

    private var execute: ((T) -> Unit)? = {}

    private var checkAction: (() -> Boolean)? = { true }

    constructor(execute: ((T) -> Unit)) {
        this.execute = execute
    }

    /**
     * @param execute
     * @param checkAction 判断是否可以执行
     * @return T类型
     */
    constructor(execute: ((T) -> Unit), checkAction: (() -> Boolean) = { true }) {
        this.execute = execute
        this.checkAction = checkAction
    }

    /** 执行带参数的action */
    fun executeAction(params: T) {
        if (checkAction == null) {
            execute?.invoke(params)
        } else {
            checkAction?.let {
                it().yes {
                    execute?.invoke(params)
                }
            }
        }
    }
}
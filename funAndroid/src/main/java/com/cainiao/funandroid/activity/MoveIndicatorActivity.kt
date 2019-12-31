package com.cainiao.funandroid.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cainiao.funandroid.R
import com.cainiao.funcommonlibrary.util.LogUtil

/**
 * Kotlin Android 扩展 改进对 Android 开发的支持
 * 从 "main" 源集中导入指定布局文件中所有控件的属性使用
 *
 * import kotlinx.android.synthetic.main.＜布局＞.* 来直接使用布局中所有的控件的属性
 *
 */
import kotlinx.android.synthetic.main.activity_move_indicator.*

class MoveIndicatorActivity : AppCompatActivity() {
    val TAG = "MoveIndicatorActivity"
    private var mContext: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_move_indicator)

        initData() // 初始化数据
        viewpager
    }

    private fun initData() {
        val width = tv_kotlin.layout.width
        val height = tv_kotlin.layout.height

        tv_kotlin.setText("kotlin")

        LogUtil.e(TAG, "width = " + width + " ; height = " + height)



        // int[] arrayImage = new int[]{}
    }

}

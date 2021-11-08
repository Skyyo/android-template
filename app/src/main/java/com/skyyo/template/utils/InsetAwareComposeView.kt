package com.skyyo.template.utils

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy

/**
 * to help with https://github.com/chrisbanes/accompanist/issues/155.
 *
 * At the moment Compose will layout on the first [onMeasure] call. Now for fragments, they usually
 * will not receive [WindowInsets] (via [dispatchApplyWindowInsets] until the
 * second (or third) [onMeasure]. This creates the flicker.
 *
 * This class can be used in place of [ComposeView]. It ignores any [onMeasure] calls
 * until we've received some insets. This is dangerous though, as a view may never receive any
 * [WindowInsets] for valid reasons. To try and combat that, there's a limit to how many
 * [onMeasure] calls can be ignored.
 */
class InsetAwareComposeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var measureCalls = 0
    private var applyInsetsRequested = false
    private val composeView: ComposeView = ComposeView(context)

    init {
        addView(
            composeView.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            },
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    @Suppress("DEPRECATION")
    override fun requestFitSystemWindows() {
        applyInsetsRequested = true
        super.requestFitSystemWindows()
    }

    override fun dispatchApplyWindowInsets(insets: WindowInsets?): WindowInsets {
        return super.dispatchApplyWindowInsets(insets).also {
            applyInsetsRequested = false
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (measureCalls < MAXIMUM_ONMEASURE_IGNORED && applyInsetsRequested) {
            setMeasuredDimension(
                MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec)
            )
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }

        measureCalls++
    }

    /**
     * Set the Jetpack Compose UI content for this view.
     *
     * @see [ComposeView.setContent].
     */
    fun setContent(content: @Composable () -> Unit) = composeView.setContent(content)

    companion object {
        private const val MAXIMUM_ONMEASURE_IGNORED = 5
    }
}

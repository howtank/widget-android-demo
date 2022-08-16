package com.howtank.howtankwidgetsample

import android.os.Bundle
import android.view.View
import com.howtank.widget.data.HTPurchaseParameters
import com.howtank.widget.main.HowtankWidget
import com.howtank.widget.main.HowtankWidgetEvent
import com.howtank.widget.main.HowtankWidgetHandler
import kotlinx.android.synthetic.main.activity_main.*
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    // Please replace by your HOST_ID here:
    private val hostId = "shire"
    private val sandbox = true
    private val verbose = true


    //================================================================================
    // Create method.
    // It's where you init the widget
    //================================================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /// Inits the widget. This has to be called before "browse".
        HowtankWidget.getInstance()
                .setHandler(object : HowtankWidgetHandler {
                    override fun onLinkSelected(link: String?) {
                        if (link != null) {
                            appendText("Link selected $link")
                        }
                    }

                    override fun widgetEvent(event: HowtankWidgetEvent?) {
                        when (event) {
                            HowtankWidgetEvent.INITIALIZED -> {
                                appendText("Widget is initialized")
                                moreActionsView.visibility = View.VISIBLE
                            }
                            HowtankWidgetEvent.OPENED -> appendText("Widget is opened")
                            HowtankWidgetEvent.DISABLED -> appendText("Widget disabled")
                            HowtankWidgetEvent.MOVED -> appendText("Widget buddle moved")
                            HowtankWidgetEvent.DISPLAYED -> appendText("Widget is displayed")
                            HowtankWidgetEvent.HIDDEN -> appendText("Widget is hidden")
                            else -> {}
                        }
                    }

                    override fun widgetUnavailable(reason: String?) {
                        appendText("Howtank widget is not available: $reason")
                    }

                })
                .setVerboseOn(verbose)
                .useSandboxPlatform()
                //.setCustomImages(INACTIVE_IMAGE_RESOURCE, ACTIVE_IMAGE_RESOURCE) // custom bubble images
                .init(this, hostId)

        actionsTextView.text = ""
        actionsTextView.movementMethod = ScrollingMovementMethod()
        moreActionsView.visibility = View.INVISIBLE
        appendText("Widget is initialized for host id '$hostId' using sandbox: $sandbox")

        initButtons()
    }

    private fun initButtons() {
        showDefaultPositionButton.setOnClickListener {
            // Shows the widget at the default position. Typically, this method is called on each activity "onResume"
            HowtankWidget.getInstance().browse(this, true, "Page with default position", "http://c1.howtank.com/default")
        }

        showCustomPositionButton.setOnClickListener {
            // Shows the widget at a custom position. Typically, this method is called on each activity "onResume"
            HowtankWidget.getInstance().browse(this, true, "Page with custom position", "http://c1.howtank.com/custom", "10 10 - -")
        }

        hideButton.setOnClickListener {
            /// Hides the widget. Typically, this method is called on a new activity "onResume"
            HowtankWidget.getInstance().browse(this, false, "Page with hidden widget", "http://c1.howtank.com/hidden")
        }

        openButton.setOnClickListener {
            // Forces the widget to open, wether it's hidden or displayed
            HowtankWidget.getInstance().open()

            // You can also call the "collapse" method to force close the widget window
            //HowtankWidget.getInstance().collapse()
        }

        conversionButton.setOnClickListener {
            // This method must be triggered when a conversion is made on the app (purchase, action...). Purchase parameters are optional
            val purchaseParameters = HTPurchaseParameters()
            purchaseParameters.isNewBuyer = true
            purchaseParameters.purchaseId = "123456"
            purchaseParameters.valueAmount = 12.3
            purchaseParameters.valueCurrency = "EUR"

            HowtankWidget.getInstance().conversion("purchase", purchaseParameters)
        }

        clearLogsTextView.setOnClickListener {
            actionsTextView.text = ""
        }


    }

    private fun appendText(text: String) {
        val newText = actionsTextView.text.toString() + "- " + text + "\n"
        this.actionsTextView.text = newText
        if (actionsTextView.layout != null) {
            val scrollAmount = actionsTextView.layout.getLineTop(actionsTextView.lineCount) - actionsTextView.height
            if (scrollAmount > 0) {
                actionsTextView.scrollTo(0, scrollAmount)
            } else {
                actionsTextView.scrollTo(0, 0)
            }
        }
    }
}

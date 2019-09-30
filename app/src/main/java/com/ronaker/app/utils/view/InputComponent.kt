package com.ronaker.app.utils.view

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ronaker.app.R
import com.ronaker.app.utils.extension.getParentActivity


@BindingAdapter("inputMutableText")
fun setMutableInputText(view: InputComponent, text: MutableLiveData<String>?) {

    val parentActivity: AppCompatActivity? = view.getParentActivity()

    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value -> view.text = value ?: "" })
    }
}


class InputComponent constructor(context: Context, attrs: AttributeSet) :
    LinearLayout(context, attrs),
    View.OnFocusChangeListener {

    enum class AlertType {
        Info,
        Error,
        Success
    }

    enum class InputMode {
        Editable,
        NoneEditable,
        DropDown
    }

    enum class InputAlertType {
        Empty,
        Filled,
        Clear
    }


    enum class InputValidationMode {
        NONE, AUTO
    }

    fun checkValid(): Boolean {
        var field = isValid
        if (!field) {
            showNotValidAlert()
        } else
            hideAlert()

        return field
    }

    var isValid: Boolean = false
        get() {
            return regex?.matches(input_edit.text) ?: true

        }


    var isTransparent: Boolean = true
        set(value) {
            field = value



            title_text.setTextColor(


                if (isTransparent) ContextCompat.getColor(
                    context,
                    R.color.colorTextLight
                ) else ContextCompat.getColor(
                    context,
                    R.color.colorTextDark
                )

            )
            input_edit.setTextColor(
                if (isTransparent) ContextCompat.getColor(
                    context,
                    R.color.colorTextLight
                ) else ContextCompat.getColor(
                    context,
                    R.color.colorTextDark
                )

            )

            input_view.setTextColor(
                if (isTransparent) ContextCompat.getColor(
                    context,
                    R.color.colorTextLight
                ) else ContextCompat.getColor(
                    context,
                    R.color.colorTextDark
                )

            )

            alert_text.setTextColor(
                if (isTransparent) ContextCompat.getColor(
                    context,
                    R.color.colorTextLight
                ) else ContextCompat.getColor(
                    context,
                    R.color.colorTextDark
                )

            )


        }

    var counter: Int = 0
        set(value) {
            field = value

            counter_text.setTextColor(
                if (isTransparent) ContextCompat.getColor(
                    context,
                    R.color.colorTextLight
                ) else ContextCompat.getColor(context, R.color.colorTextDark)
            )


            counter_text.visibility = if (field > 0) View.VISIBLE else View.GONE


            counter_text.text = String.format("0/%d",value)


        }

    var hasInputDotValidator: Boolean = true
        set(value) {
            field = value

            inputState_image.visibility = if (field) View.VISIBLE else GONE
        }

    var inputValidationMode: InputValidationMode = InputValidationMode.NONE
        set(value) {
            field = value
        }


    var autofillHints: String? = null
        set(value) {
            field = value

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                input_edit.setAutofillHints(value)
            }

        }


    var inputMode: InputMode = InputMode.Editable
        set(value) {
            field = value


            when (field) {
                InputMode.Editable -> {

                    input_edit.visibility = View.VISIBLE
                    input_view.visibility = View.GONE
                }
                InputMode.NoneEditable -> {

                    input_edit.visibility = View.GONE
                    input_view.visibility = View.VISIBLE
                }

                InputMode.DropDown -> {

                }
            }


        }


    var container_layout: ConstraintLayout
    var title_text: TextView
    var input_edit: EditText
    var inputState_image: ImageView
    var input_line: LinearLayout
    var alert_layer: LinearLayout
    var alert_image: ImageView
    var pass_image: ImageView
    var alert_text: TextView

    var input_view: TextView

    lateinit var counter_text: TextView

    fun addTextChangedListener(watcher: TextWatcher) {
        input_edit.addTextChangedListener(watcher)
    }

    fun inputRequestFocus() {
        input_edit.requestFocus()
    }

    var title: String? = null
        set(value) {
            field = value

            title_text.setText(value)
        }


    var maxLength: Int = 0
        set(value) {
            field = value

            if (value == 0) {
                input_edit.filters.toMutableList().apply {

                    removeAll {
                        it.javaClass == InputFilter.LengthFilter::class.java
                    }
                }

            } else if (input_edit.filters.isNullOrEmpty()) {
                input_edit.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(value))

            } else {
                input_edit.filters.toMutableList().apply {

                    removeAll {

                        it.javaClass == InputFilter.LengthFilter::class.java
                    }
                    this.add(InputFilter.LengthFilter(value))
                }
            }

        }


    var text: String? = null
        set(value) {
            field = value

            input_edit.setText(value)
            input_view.setText(value)
        }
        get() {
            return input_edit.text.toString()
        }

    var hint: String? = null
        set(value) {
            field = value

            input_edit.setHint(value)
            input_view.setHint(value)
        }
    var is_alert: Boolean = true
        set(value) {
            field = value

            alert_layer.visibility = if (value) View.VISIBLE else View.GONE
        }

    var regex: Regex? = null


    var imeOptions: Int = 0
        set(value) {
            field = value

            input_edit.imeOptions = value
        }


    var inputType: Int = EditorInfo.TYPE_NULL
        set(value) {
            field = value
            input_edit.inputType = value

            if (input_edit.inputType == (EditorInfo.TYPE_TEXT_VARIATION_PASSWORD or EditorInfo.TYPE_CLASS_TEXT)) {

                inputState_image.visibility = View.GONE
                pass_image.visibility = View.VISIBLE
                pass_image.setImageResource(R.drawable.ic_pass_hide)
            } else if (input_edit.inputType == EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD or EditorInfo.TYPE_CLASS_TEXT) {


                inputState_image.visibility = View.GONE
                pass_image.visibility = View.VISIBLE

                pass_image.setImageResource(R.drawable.ic_pass_show)
            }

        }


    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun afterTextChanged(editable: Editable) {

            textChangeValid(editable)

            if (counter > 0) counter_text.text = String.format("%d/%d",editable.length,counter)

        }
    }


    private fun textChangeValid(editable: Editable) {


        if (editable.isEmpty()) {
            showInputIcon(InputAlertType.Empty)
            hideAlert()
        } else if (input_edit.isFocused)
            showInputIcon(InputAlertType.Clear)
        else if (regex?.matches(editable) == true) {
            showInputIcon(InputAlertType.Filled)
            hideAlert()
        } else {
            showInputIcon(InputAlertType.Empty)
            showNotValidAlert()
        }


    }


    init {

        LayoutInflater.from(context)
            .inflate(R.layout.component_input, this, true)



        container_layout = findViewById(R.id.container_layout)
        title_text = findViewById(R.id.title_text)
        input_edit = findViewById(R.id.input_edit)
        inputState_image = findViewById(R.id.inputState_image)
        input_line = findViewById(R.id.input_line)
        alert_layer = findViewById(R.id.alert_layer)
        alert_image = findViewById(R.id.alert_image)
        alert_text = findViewById(R.id.alert_text)
        counter_text = findViewById(R.id.counter_text)
        input_view = findViewById(R.id.input_view)

        pass_image = findViewById(R.id.inputPassword_image)


        input_edit.onFocusChangeListener = this

        input_edit.addTextChangedListener(this.watcher)

        pass_image.setOnClickListener {

            if (input_edit.inputType == EditorInfo.TYPE_TEXT_VARIATION_PASSWORD or EditorInfo.TYPE_CLASS_TEXT) {
                inputType =
                    EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD or EditorInfo.TYPE_CLASS_TEXT
            } else if (input_edit.inputType == EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD or EditorInfo.TYPE_CLASS_TEXT) {

                inputType = EditorInfo.TYPE_TEXT_VARIATION_PASSWORD or EditorInfo.TYPE_CLASS_TEXT
            }
        }

        orientation = VERTICAL

        attrs.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.input_component_attributes, 0, 0
            )



            title =
                typedArray
                    .getString(
                        R.styleable
                            .input_component_attributes_input_title
                    )



            hint =
                typedArray
                    .getString(
                        R.styleable
                            .input_component_attributes_input_hint
                    )





            is_alert = typedArray
                .getBoolean(
                    R.styleable
                        .input_component_attributes_input_is_alert,
                    true
                )




            isTransparent = typedArray.getBoolean(
                R.styleable.input_component_attributes_input_transparent,
                true
            )


            hasInputDotValidator =
                typedArray.getBoolean(R.styleable.input_component_attributes_input_dot, true)

            counter = typedArray.getInteger(R.styleable.input_component_attributes_input_counter, 0)


            inputType = typedArray.getInt(
                R.styleable.input_component_attributes_android_inputType,
                EditorInfo.TYPE_NULL
            )


            imeOptions = typedArray.getInt(
                R.styleable.input_component_attributes_android_imeOptions,
                0
            )

            maxLength = typedArray.getInt(
                R.styleable.input_component_attributes_android_maxLength,
                0
            )

            autofillHints = typedArray.getString(
                R.styleable.input_component_attributes_android_autofillHints
            )

            var regexString = typedArray.getString(
                R.styleable
                    .input_component_attributes_input_regex
            )


            regex = regexString?.toRegex()


            inputState_image.setOnClickListener {

                text = ""
                hideAlert()
            }


            inputValidationMode = InputValidationMode.values()[typedArray
                .getInt(
                    R.styleable
                        .input_component_attributes_input_validation_mode,
                    1
                )];


            inputMode = InputMode.values()[typedArray
                .getInt(
                    R.styleable
                        .input_component_attributes_input_mode,
                    0
                )];





            input_edit.typeface = Typeface.DEFAULT
            input_view.typeface = Typeface.DEFAULT
            typedArray.recycle()
        }
    }


    fun showAlert(type: AlertType, message: String) {
        alert_text.text = message

        alert_image.setImageResource(
            when (type) {
                AlertType.Error -> R.drawable.ic_guide_red
                AlertType.Info -> if (isTransparent) R.drawable.ic_guide_dark else R.drawable.ic_guide_light
                AlertType.Success -> R.drawable.ic_guide_success
            }


        )


        alert_text.animate().setDuration(200).alpha(1.0f).start()
        alert_image.animate().setDuration(200).alpha(1.0f).start()
    }

    fun showNotValidAlert() {


        showAlert(AlertType.Error, title + " is not valid.")

    }

    fun hideAlert() {
        alert_text.animate().setDuration(200).alpha(0.0f).start()
        alert_image.animate().setDuration(200).alpha(0.0f).start()


    }


    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) {
            textChangeValid(input_edit.text)
            input_line.setBackgroundColor(
                if (isTransparent) ContextCompat.getColor(
                    context,
                    R.color.white
                ) else ContextCompat.getColor(context, R.color.colorNavy)
            )

        } else {
            textChangeValid(input_edit.text)
            input_line.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPlatinGrey))
        }
    }


    fun showInputIcon(type: InputAlertType) {

        inputState_image.setImageResource(
            when (type) {
                InputAlertType.Clear -> R.drawable.ic_field_clear
                InputAlertType.Empty -> R.drawable.ic_field_empty
                InputAlertType.Filled -> R.drawable.ic_field_filled
            }
        )


        inputState_image.isClickable =
            when (type) {
                InputAlertType.Clear -> true
                InputAlertType.Empty -> false
                InputAlertType.Filled -> false
            }

    }


}
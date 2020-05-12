package com.ronaker.app.ui.addProduct

import android.os.Build
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ronaker.app.model.Category
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class AddProductViewModelTest {


    // Subject under test
    private lateinit var addProductViewModel: AddProductViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {

        addProductViewModel = AddProductViewModel(ApplicationProvider.getApplicationContext())

    }


    @Test
    fun selectCategory_setNewCategory() {

        val category = Category("sdsd", "mainCategory", "url", null)

        addProductViewModel.selectCategory(category)

        val value = addProductViewModel.productSubCategoryVisibility.value

        assertThat(value, equalTo(View.VISIBLE) )

        assertThat( addProductViewModel.categories.size,  equalTo(1) )

    }

}
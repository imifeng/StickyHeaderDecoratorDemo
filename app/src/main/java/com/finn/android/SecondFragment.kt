package com.finn.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.finn.android.adapter.TestRecyclerAdapter
import com.finn.android.data.TextDataUtils
import com.finn.android.decorator.StickyHeaderDecorator
import com.finn.android.extension.hide
import com.finn.android.extension.inflate
import com.finn.android.extension.show
import kotlinx.android.synthetic.main.fragment_second.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private val testRecyclerAdapter by lazy {
        TestRecyclerAdapter()
    }
    private val linearLayoutManager by lazy {
        LinearLayoutManager(context)
    }
    private val stickyHeaderDecorator by lazy {
        StickyHeaderDecorator(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        initEvent()
    }

    private fun initView() {
        with(rv_view) {
            layoutManager = linearLayoutManager
            adapter = testRecyclerAdapter
            addItemDecoration(stickyHeaderDecorator)
        }

        val textData = TextDataUtils().getTestData()
        textData.sortBy { it.title }//排序
        val list = textData.map { bean -> bean.title }
        with(stickyHeaderDecorator){
            hideCategoryHeader = { isHide ->
                //为了测试隐藏和显示按钮
                if (isHide) {
                    btn_top.hide()
                    btn_middle.hide()
                    btn_end.hide()
                } else {
                    btn_top.show()
                    btn_middle.show()
                    btn_end.show()
                }
            }
            updateCategoryHeader = {
                //TODO Something
                Toast.makeText(requireContext(), "你已滚动至=${it}", Toast.LENGTH_SHORT).show()
            }
            setCategoryList(list)
        }
        testRecyclerAdapter.addAllItems(textData)
    }

    private fun initEvent() {
        button_back.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        //下面按钮为了测试
        btn_top.setOnClickListener {
            //
            val position = stickyHeaderDecorator.categoryHeaderMap[btn_top.text.toString()]
            smoothMoveToPosition(position)
        }

        btn_middle.setOnClickListener {
            val position = stickyHeaderDecorator.categoryHeaderMap[btn_middle.text.toString()]
            smoothMoveToPosition(position)
        }

        btn_end.setOnClickListener {
            val position = stickyHeaderDecorator.categoryHeaderMap[btn_end.text.toString()]
            smoothMoveToPosition(position)
        }
    }

    private fun smoothMoveToPosition(position: Int?) {
        position?.let {
            linearLayoutManager.scrollToPositionWithOffset(it, 0)
            linearLayoutManager.stackFromEnd = true
        }
    }
}
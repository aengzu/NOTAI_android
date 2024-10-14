package com.iguana.notetaking.ai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.iguana.notetaking.databinding.FragmentAiBinding
import androidx.fragment.app.viewModels
import com.iguana.notetaking.NotetakingActivity
import com.iguana.notetaking.NotetakingViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AiFragment : Fragment() {

    companion object {
        fun newInstance() = AiFragment()
    }

    private var _binding: FragmentAiBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AiViewModel by viewModels()

    private val notetakingViewModel: NotetakingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAiBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 현재 페이지가 Fragment가 생성될 때 설정되도록 함
        val currentPage = (activity as? NotetakingActivity)?.getCurrentPage() ?: 1
        updateContentForPage(currentPage)

        super.onViewCreated(view, savedInstanceState)
        viewModel.pageNumber.observe(viewLifecycleOwner) { pageNumber ->
            binding.aiPageTextView.text = pageNumber?.toString()+" 페이지"
        }

        // AI 요약 버튼 클릭 리스너 정의
        binding.aiButton.setOnClickListener {
            // 1부터 현재 페이지까지의 페이지 목록을 생성하여 요청
            // TODO : 후에 페이지 선택으로 수정
            val pagesToSummarize = (1..currentPage+1).toList()
            requestAISummarization(pagesToSummarize)
        }
    }

    // 페이지 번호 업데이트 메서드
    fun updateContentForPage(pageNumber: Int) {
        if (isAdded && !isDetached) { // Fragment가 활성 상태인지 확인
            viewModel.setPageNumber(pageNumber+1)
        }
    }

    // AI 요약 요청
    private fun requestAISummarization(pages: List<Int>) {
        val documentId = notetakingViewModel.pdfId
        viewModel.requestAISummarization(documentId, pages)

        binding.aiStatusTextView.text = "AI 요약 중입니다"
        binding.aiButton.isEnabled = false
        binding.buttonText.text = "요약 중.."
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

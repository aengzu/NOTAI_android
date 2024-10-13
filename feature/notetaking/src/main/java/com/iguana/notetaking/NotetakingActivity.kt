package com.iguana.notetaking

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.iguana.notetaking.databinding.ActivityNotetakingBinding
import com.iguana.notetaking.sidebar.SideBarFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NotetakingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotetakingBinding
    private val viewModel: NotetakingViewModel by viewModels()

    companion object {
        const val PDF_URI_KEY = "PDF_URI"
        const val PDF_TITLE_KEY = "PDF_TITLE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setUpBinding()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Intent에서 PDF URI 받기
        val pdfUriString = intent.getStringExtra(PDF_URI_KEY)
        val pdfTitle = intent.getStringExtra(PDF_TITLE_KEY) ?: "무제"
        updateTitle(pdfTitle)

        setUpToolbar()

        // PDF URI가 있는 경우 프래그먼트 추가
        if (pdfUriString != null) {
            setupPdfViewerAndSideBar(Uri.parse(pdfUriString))
        } else {
            Log.e("NotetakingActivity", "PDF URI is null in Activity") // URI가 null인 경우 로그
        }
    }
    // PDF 뷰어 및 사이드바 설정
    private fun setupPdfViewerAndSideBar(pdfUri: Uri) {
        setupPdfViewer(pdfUri)
        setupSideBar()
        setupToolbarActions()
    }


    private fun setUpBinding() {
        binding = ActivityNotetakingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setUpToolbar() {
        binding.titleBar.backButton.setOnClickListener {
            finish()
        }
    }

    // PDF 뷰어 설정
    private fun setupPdfViewer(pdfUri: Uri) {
        val pdfViewerFragment = PdfViewerFragment.newInstance(pdfUri)
        supportFragmentManager.beginTransaction()
            .replace(R.id.pdf_fragment_container, pdfViewerFragment)
            .commit()
    }

    // 툴바 액션 설정
    private fun setupToolbarActions() {
        // 텍스트 추가
        binding.toolbar.btnText.setOnClickListener {
            getPdfViewerFragment()?.getCurrentPdfPageFragment()?.addTextBox()
        }

        // AI 탭 전환
        binding.toolbar.btnAI.setOnClickListener {
            showSideBar()
            switchToTab(SideBarFragment.Tab.AI)
        }

        // 녹음 탭 전환
        binding.toolbar.btnRecord.setOnClickListener {
            showSideBar()
            switchToTab(SideBarFragment.Tab.RECORD)
        }
    }

    // 사이드바 보이기
    private fun showSideBar() {
        binding.sideBarContainer.visibility = View.VISIBLE
    }

    // 사이드바 탭 전환
    private fun switchToTab(tab: SideBarFragment.Tab) {
        val sideBarFragment = supportFragmentManager.findFragmentById(R.id.side_bar_container) as? SideBarFragment
        sideBarFragment?.switchToTab(tab)
    }

    // 사이드바 프래그먼트 설정
    private fun setupSideBar() {
        val sideBarFragment = SideBarFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.side_bar_container, sideBarFragment)
            .commit()
    }


    // 타이틀 업데이트
    private fun updateTitle(title: String) {
        binding.titleBar.titleBar.text = title
    }

    // 페이지 변경 시 호출되는 메서드
    fun onPageChanged(pageNumber: Int) {
        // 사이드바 프래그먼트 해당 페이지 내용으로 업데이트
        val sideBarFragment = supportFragmentManager.findFragmentById(R.id.side_bar_container) as? SideBarFragment
        sideBarFragment?.updatePageContent(pageNumber)
    }

    // 현재 페이지 번호 가져오는 메서드
    fun getCurrentPage(): Int? {
        val pdfViewerFragment = supportFragmentManager.findFragmentById(R.id.pdf_fragment_container) as? PdfViewerFragment
        return pdfViewerFragment?.getCurrentPage()
    }

    // 현재 PDF 뷰어 프래그먼트 가져오기
    private fun getPdfViewerFragment(): PdfViewerFragment? {
        return supportFragmentManager.findFragmentById(R.id.pdf_fragment_container) as? PdfViewerFragment
    }
}
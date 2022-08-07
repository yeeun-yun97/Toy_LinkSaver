package com.github.yeeun_yun97.toy.linksaver.ui.fragment.share_link

import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentSaveBackupBinding
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.recycler.ShareListAdapter
import com.github.yeeun_yun97.clone.ynmodule.ui.component.YnConfirmBaseDialogFragment
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.BackupViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SaveBackupFragment @Inject constructor() : SjBasicFragment<FragmentSaveBackupBinding>() {
    private val viewModel: BackupViewModel by viewModels()

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun layoutId(): Int = R.layout.fragment_save_backup
    override fun onCreateView() {
        val handlerMap = hashMapOf<Int, () -> Unit>(R.id.menu_save to ::save)
        binding.toolbar.setMenu(R.menu.toolbar_menu_edit, handlerMap)

        initRecyclerView()

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                handleActivityResult(it)
            }
    }

    override fun initRecyclerView() {
        binding.entityRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = ShareListAdapter()
        binding.entityRecyclerView.adapter = adapter
        viewModel.wrapUpList.observe(viewLifecycleOwner) {
            adapter.setList(it)
        }
    }

    private fun saveModels() {
        val fileName = "linkTagBackup.txt"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/plain"
                putExtra(Intent.EXTRA_TITLE, fileName)
            }
            activityResultLauncher.launch(intent)
        }
    }

    private fun handleActivityResult(it: ActivityResult) {
        if (it.resultCode == -1) {
            val uri = it.data!!.data!!
            val outputStream = requireActivity().contentResolver.openOutputStream(uri)!!
            viewModel.write(outputStream)
            outputStream.close()
        } else {
            val messageDialog = YnConfirmBaseDialogFragment("실패", "백업 파일 저장에 실패하였습니다", null)
            messageDialog.show(childFragmentManager, "파일 저장 실패")
        }
    }

    private fun save() {
        saveModels()
    }
}
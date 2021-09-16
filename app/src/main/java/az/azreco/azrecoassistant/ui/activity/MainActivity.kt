package az.azreco.azrecoassistant.ui.activity

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import az.azreco.azrecoassistant.R
import az.azreco.azrecoassistant.constants.Constants.NOTIFICATION_LISTENERS_PARAMETERS
import az.azreco.azrecoassistant.databinding.ActivityMainBinding
import az.azreco.azrecoassistant.ui.viewmodel.MainViewModel
import az.azreco.azrecoassistant.util.PermissionUtil
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navHostFragment: NavHostFragment

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermissions()
        setupUI()
        fabClickListener()
        profileIconClickListener()
        viewModel.initContacts()
    }

    private fun fabClickListener() = binding.mainFab.setOnClickListener {
        if (PermissionUtil.hasPermissions(this)) {
            startActivity(Intent(this, DialogActivity::class.java))
        } else {
            requestPermissions()
        }
    }

    private fun profileIconClickListener() = binding.mainProfileIv.setOnClickListener {
        Toast.makeText(this, "LOOOL", Toast.LENGTH_SHORT).show()
    }

    private fun setupUI() {
        setupNavView()
        initNavHostFragment()
        setupToolbar()
        setupNavigationController()
    }

    private fun setupNavView() = binding.apply {
        bottomNavView.menu.getItem(2).isEnabled = false
    }


    private fun initNavHostFragment() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
    }

    private fun setupNavigationController() {
        binding.bottomNavView.setupWithNavController(navHostFragment.findNavController())
        navHostFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
//                when (destination.id) {
//
//                }
                showBottomBar()
            }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarMain)
        supportActionBar?.title = ""
    }


    private fun hideBottomBar() = binding.apply {
        supportActionBar?.setIcon(null)
        bottomAppBar.isVisible = false
        toolbarMain.setNavigationIcon(R.drawable.ic_back)
    }


    private fun showBottomBar() = binding.apply {
        toolbarMain.navigationIcon = null
        supportActionBar?.setIcon(R.drawable.ic_idrak)
        bottomAppBar.isVisible = true
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun requestPermissions() {
        if (PermissionUtil.hasPermissions(this)) {
//            viewModel.initContacts()
            return
        }
        EasyPermissions.requestPermissions(
            this,
            "You need to accept accessibility permissions to use this app.",
            1,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) =
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}
package com.eltex.androidschool.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentBottomNavigationBinding

class BottomNavigationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        val binding = FragmentBottomNavigationBinding.inflate(inflater, container, false)

        val navController =
            requireNotNull(childFragmentManager.findFragmentById(R.id.container)).findNavController()

        binding.bottomNavigation.setupWithNavController(navController)

        val newPostListener = View.OnClickListener {

            //TODO
        }

        val newEventListener = View.OnClickListener {
            findNavController().navigate(R.id.action_bottomNavigationFragment_to_newEventFragment)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.postsFragment -> {
                    binding.newEvent.setOnClickListener(newPostListener)
                    binding.newEvent.animate()
                        .scaleY(1F)
                        .scaleX(1F)
                }

                R.id.eventsFragment -> {
                    binding.newEvent.setOnClickListener(newEventListener)
                    binding.newEvent.animate()
                        .scaleY(1F)
                        .scaleX(1F)
                }

                R.id.usersFragment -> {
                    binding.bottomNavigation.setOnClickListener(null)
                    binding.newEvent.animate()
                        .scaleY(0F)
                        .scaleX(0F)
                }

            }
        }

        return binding.root
    }
}
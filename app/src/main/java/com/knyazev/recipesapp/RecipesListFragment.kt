package com.knyazev.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.knyazev.recipesapp.databinding.FragmentRecipesListBinding

class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalArgumentException("Binding for FragmentRecipesListBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = RecipesListFragment()
    }
}
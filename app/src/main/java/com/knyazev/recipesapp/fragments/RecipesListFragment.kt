package com.knyazev.recipesapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.knyazev.recipesapp.ARG_CATEGORY_ID
import com.knyazev.recipesapp.ARG_CATEGORY_IMAGE_URL
import com.knyazev.recipesapp.ARG_CATEGORY_NAME
import com.knyazev.recipesapp.R
import com.knyazev.recipesapp.STUB
import com.knyazev.recipesapp.adapters.RecipesListAdapter
import com.knyazev.recipesapp.databinding.FragmentRecipesListBinding

class RecipesListFragment : Fragment() {

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalArgumentException("Binding for FragmentRecipesListBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("!!!", "onCreateView in RecipesListFragment")
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("!!!", "onViewCreated in RecipesListFragment")
        super.onViewCreated(view, savedInstanceState)
        categoryId = requireArguments().getInt(ARG_CATEGORY_ID)
        categoryName = requireArguments().getString(ARG_CATEGORY_NAME)
        categoryImageUrl = requireArguments().getString(ARG_CATEGORY_IMAGE_URL)
        initRecycler()
    }

    private fun initRecycler() {
        Log.d("!!!", "initRecycler in RecipesListFragment")
        val listAdapter = RecipesListAdapter(STUB.getRecipesByCategoryId(categoryId))
        binding.rvRecipes.adapter = listAdapter

//        listAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
//            override fun onItemClick(recipeId: Int) {
//                Log.d("!!!", "initRecycler in onItemClick in RecipesListFragment" + recipeId + STUB.getRecipesByCategoryId(categoryId))
//                openRecipeByRecipeId(STUB.getRecipesByCategoryId(categoryId)[recipeId].id)
//            }
//        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        Log.d("!!!", "openRecipeByRecipeId in RecipesListFragment")
        binding.rvRecipes[recipeId].setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace<RecipesListFragment>(R.id.mainContainer)
            }
        }
    }
}
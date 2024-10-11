package com.knyazev.recipesapp.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryId = requireArguments().getInt(ARG_CATEGORY_ID)
        categoryName = requireArguments().getString(ARG_CATEGORY_NAME)

        categoryImageUrl = requireArguments().getString(ARG_CATEGORY_IMAGE_URL)
        val drawable = try {
            Drawable.createFromStream(
                view.context.assets.open(categoryImageUrl!!),
                null
            )
        } catch (e: NullPointerException) {
            Log.d("logTag", "Image not found $categoryImageUrl")
            null
        }
        binding.imageView2.setImageDrawable(drawable)
        binding.tvHeaderRecipe.text = categoryName
        initRecycler()
    }

    private fun initRecycler() {
        val listAdapter = RecipesListAdapter(STUB.getRecipesByCategoryId(categoryId))
        binding.rvRecipes.adapter = listAdapter
        listAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainContainer)
        }
    }
}
package com.knyazev.recipesapp.fragments

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.knyazev.recipesapp.ARG_RECIPE
import com.knyazev.recipesapp.R
import com.knyazev.recipesapp.adapters.IngredientsAdapter
import com.knyazev.recipesapp.adapters.MethodAdapter
import com.knyazev.recipesapp.databinding.FragmentRecipeBinding
import com.knyazev.recipesapp.entities.Recipe

class RecipeFragment : Fragment() {

    private var recipe: Recipe? = null

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalArgumentException("Binding for FragmentRecipeBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(ARG_RECIPE, Recipe::class.java)
        } else requireArguments().getParcelable(ARG_RECIPE)
        initUI(view)
        initRecycler()
    }

    private fun initUI(view: View) {
        val recipeImageUrl = recipe?.imageUrl
        val drawable = try {
            Drawable.createFromStream(view.context.assets.open(recipeImageUrl!!), null)
        } catch (e: NullPointerException) {
            Log.d("logTag", "Image not found $recipeImageUrl")
            null
        }
        binding.ivHeaderRecipe.setImageDrawable(drawable)
        binding.tvHeaderRecipe.text = recipe?.title
    }

    private fun initRecycler() {
        val divider =
            MaterialDividerItemDecoration(requireContext(), MaterialDividerItemDecoration.VERTICAL)
        divider.isLastItemDecorated = false
        divider.setDividerColorResource(requireContext(), R.color.divider)
        divider.setDividerInsetStartResource(requireContext(), R.dimen.main_space_8)
        divider.setDividerInsetEndResource(requireContext(), R.dimen.main_space_8)
        val ingredientAdapter = IngredientsAdapter(recipe?.ingredients ?: emptyList())
        val methodAdapter = MethodAdapter(recipe?.method ?: emptyList())

        binding.rvIngredients.adapter = ingredientAdapter
        binding.rvMethod.adapter = methodAdapter
        binding.rvMethod.addItemDecoration(divider)
        binding.rvIngredients.addItemDecoration(divider)

        binding.sbPortions.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                ingredientAdapter.updateIngredients(progress)
                ingredientAdapter.notifyDataSetChanged()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
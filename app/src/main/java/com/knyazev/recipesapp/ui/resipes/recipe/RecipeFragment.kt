package com.knyazev.recipesapp.ui.resipes.recipe

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.knyazev.recipesapp.Constants.ARG_RECIPE
import com.knyazev.recipesapp.Constants.MIN_PORTIONS
import com.knyazev.recipesapp.Constants.PREFS_KEY_FAVORITES_CATEGORY
import com.knyazev.recipesapp.Constants.PREFS_NAME
import com.knyazev.recipesapp.R
import com.knyazev.recipesapp.databinding.FragmentRecipeBinding
import com.knyazev.recipesapp.data.entities.Recipe
import com.knyazev.recipesapp.ui.resipes.recipesList.IngredientsAdapter
import com.knyazev.recipesapp.ui.resipes.recipesList.MethodAdapter

class RecipeFragment : Fragment() {
    private var recipe: Recipe? = null
    private val sharedPref: SharedPreferences by lazy {
        requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalArgumentException("Binding for FragmentRecipeBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPref
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
        val favorites = getFavorites()
        val recipeImageUrl = recipe?.imageUrl
        val drawable = try {
            Drawable.createFromStream(view.context.assets.open(recipeImageUrl!!), null)
        } catch (e: NullPointerException) {
            Log.d("logTag", "Image not found $recipeImageUrl")
            null
        }
        binding.countPortions.text = MIN_PORTIONS
        binding.ivHeaderRecipe.setImageDrawable(drawable)
        binding.tvHeaderRecipe.text = recipe?.title
        var flag = getFavorites().contains(recipe?.id.toString())
        if (flag) {
            binding.ibHeaderHeart.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_big_red_heart
                )
            )
        } else {
            binding.ibHeaderHeart.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_big_heart
                )
            )
        }
        binding.ibHeaderHeart.setOnClickListener {
            flag = !flag
            if (flag) {
                binding.ibHeaderHeart.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_big_red_heart
                    )
                )
                favorites.add(recipe?.id.toString())
            } else {
                binding.ibHeaderHeart.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_big_heart
                    )
                )
                favorites.remove(recipe?.id.toString())
            }
            saveFavorites(favorites)
        }
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
                binding.countPortions.text = progress.toString()
                ingredientAdapter.updateIngredients(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun saveFavorites(recipeId: Set<String>) {
        sharedPref.edit().putStringSet(PREFS_KEY_FAVORITES_CATEGORY, recipeId).apply()
    }

    private fun getFavorites(): MutableSet<String> {
        val favorites =
            sharedPref.getStringSet(PREFS_KEY_FAVORITES_CATEGORY, mutableSetOf()) ?: mutableSetOf()
        return HashSet(favorites)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
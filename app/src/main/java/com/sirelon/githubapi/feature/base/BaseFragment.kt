package com.sirelon.githubapi.feature.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * Created on 2019-09-05 21:35 for GithubAPi.
 */
open class BaseFragment(
    @LayoutRes
    layoutId: Int
) : Fragment(layoutId)
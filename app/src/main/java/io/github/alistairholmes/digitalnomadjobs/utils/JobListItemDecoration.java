package io.github.alistairholmes.digitalnomadjobs.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


 // Custom item decoration for JobActivity RecyclerView. Adds a
 // small amount of padding to the bottom of grid items.
public class JobListItemDecoration extends RecyclerView.ItemDecoration {

    private int smallPadding;

     public JobListItemDecoration(int smallPadding) {
         this.smallPadding = smallPadding;
     }

     @Override
     public void getItemOffsets(Rect outRect, View view,
                                RecyclerView parent, RecyclerView.State state) {
         outRect.bottom = smallPadding;
     }

}

package co.techmagic.hr.presentation.ui.adapter.calendar;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mikepenz.fastadapter.items.AbstractItem;

public abstract class AbstractGridItem<T extends AbstractGridItem<?, ?>, VH extends RecyclerView.ViewHolder> extends AbstractItem <T, VH>{

    public void onClick(View view) {
        // Do nothing. Override!
    }
}

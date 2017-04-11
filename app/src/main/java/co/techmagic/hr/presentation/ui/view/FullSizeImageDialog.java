package co.techmagic.hr.presentation.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import co.techmagic.hr.R;

public class FullSizeImageDialog extends Dialog {

    private ImageView ivFullImageSize;
    private Context context;
    private UserPhotoActionListener actionListener;


    public FullSizeImageDialog(@NonNull Context context, int theme, @NonNull UserPhotoActionListener actionListener) {
        super(context, theme);
        this.context = context;
        this.actionListener =  actionListener;
        setupDialogView();
    }


    private void setupDialogView() {
        Window window = getWindow();
        WindowManager.LayoutParams wmlp = window.getAttributes();
        wmlp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        window.setAttributes(wmlp);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCancelable(true);

        setupUi();
    }


    private void setupUi() {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_full_size_image, null);
        ivFullImageSize = (ImageView) view.findViewById(R.id.ivFullImage);
        ImageView ivClose = (ImageView) view.findViewById(R.id.ivClose);
        ImageView ivFullSizeDownload = (ImageView) view.findViewById(R.id.ivFullSizeDownload);

        ivFullSizeDownload.setOnClickListener(v -> actionListener.onDownloadImage());
        ivClose.setOnClickListener(v -> actionListener.onCloseImage());
        setContentView(view);
    }


    public void loadImage(@Nullable String url) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.ic_user_placeholder)
                .into(ivFullImageSize);
    }
}

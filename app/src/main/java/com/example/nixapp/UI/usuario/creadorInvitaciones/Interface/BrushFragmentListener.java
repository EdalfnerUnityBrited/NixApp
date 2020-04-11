package com.example.nixapp.UI.usuario.creadorInvitaciones.Interface;

public interface BrushFragmentListener {
    void onBrushSizeChangedListener(float size);
    void onBrushOpacityChangedListener(int opacity);
    void onBrushColorChangedListener(int color);
    void onBrushStateChangedListener(boolean isEraser);
}

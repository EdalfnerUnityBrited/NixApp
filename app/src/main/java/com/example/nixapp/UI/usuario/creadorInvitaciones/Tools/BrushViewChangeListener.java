package com.example.nixapp.UI.usuario.creadorInvitaciones.Tools;

import com.example.nixapp.UI.usuario.creadorInvitaciones.Tools.BrushDrawingView;

interface BrushViewChangeListener {
    void onViewAdd(BrushDrawingView brushDrawingView);

    void onViewRemoved(BrushDrawingView brushDrawingView);

    void onStartDrawing();

    void onStopDrawing();
}

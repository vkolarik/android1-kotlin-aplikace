package cz.mendelu.project.ui.screens.Items.addedit

import android.content.Context
import android.net.Uri

interface ItemAddEditScreenActions {
    fun onItemDataChanged(data: ItemAddEditScreenData)

    fun saveItem()

    fun deleteItem(context: Context)

    fun newImagePreview(context: Context, uri: Uri)

    fun deleteImage(context: Context)

}
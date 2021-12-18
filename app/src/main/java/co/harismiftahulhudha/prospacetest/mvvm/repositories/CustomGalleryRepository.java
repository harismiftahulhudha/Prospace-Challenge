package co.harismiftahulhudha.prospacetest.mvvm.repositories;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.harismiftahulhudha.prospacetest.mvvm.models.CustomGalleryFolderModel;
import co.harismiftahulhudha.prospacetest.mvvm.models.CustomGalleryModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class CustomGalleryRepository {

    private final Context context;

    @Inject
    public CustomGalleryRepository(@ApplicationContext Context context) {
        this.context = context;
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public MutableLiveData<List<CustomGalleryModel>> files = new MediatorLiveData<>();
    public MutableLiveData<List<CustomGalleryFolderModel>> folders = new MediatorLiveData<>();
    private final MutableLiveData<List<CustomGalleryModel>> selectedFiles = new MediatorLiveData<>();

    @SuppressLint("Recycle")
    public void getFolders() {
        final List<CustomGalleryFolderModel> tmpBuckets = new ArrayList<>();
        final List<Long> tmpBucketIds = new ArrayList<>();

        executor.execute(() -> {
            final Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            final String[] projection = { MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
            final Cursor cursor = context.getContentResolver().query(
                    uriExternal,
                    projection,
                    null,
                    null,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " ASC"
            );
            if (cursor != null) {
                final int indexBucketId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
                final int indexBucketName = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                while (cursor.moveToNext()) {
                    final long bucketId = cursor.getLong(indexBucketId);
                    final String bucketName = cursor.getString(indexBucketName);
                    if (!tmpBucketIds.contains(bucketId)) {
                        final CustomGalleryFolderModel folder = new CustomGalleryFolderModel(bucketId, bucketName);
                        tmpBuckets.add(folder);
                        tmpBucketIds.add(bucketId);
                    }
                }
                cursor.close();
                folders.postValue(tmpBuckets);
            }
        });
    }

    @SuppressLint("Recycle")
    public void getFiles(String bucket) {
        final List<CustomGalleryModel> tmpFiles = new ArrayList<>();

        executor.execute(() -> {
            final Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                final String[] projection = {
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.DATE_ADDED,
                        MediaStore.Images.Media.BUCKET_ID,
                        MediaStore.Images.Media.DISPLAY_NAME,
                };
                final Cursor cursor = context.getContentResolver().query(
                        uriExternal,
                        projection,
                        MediaStore.Files.FileColumns.BUCKET_ID + "=" + bucket,
                        null,
                        MediaStore.Images.Media.DATE_ADDED + " DESC"
                );
                if (cursor != null) {
                    final int indexId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                    final int indexFilename = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                    while(cursor.moveToNext()) {
                        Uri uri = ContentUris.withAppendedId(uriExternal, cursor.getLong(indexId));
                        CustomGalleryModel model = new CustomGalleryModel(cursor.getLong(indexId), uri, cursor.getString(indexFilename));
                        tmpFiles.add(model);
                    }
                    cursor.close();
                    files.postValue(tmpFiles);
                }
            }
            else {
                final String[] projection = {
                        MediaStore.Images.Media._ID,
                        MediaStore.Files.FileColumns.DATA,
                        MediaStore.Images.Media.DATE_ADDED,
                        MediaStore.Images.Media.BUCKET_ID,
                        MediaStore.Images.Media.DISPLAY_NAME,
                };
                final Cursor cursor = context.getContentResolver().query(
                        uriExternal,
                        projection,
                        MediaStore.Files.FileColumns.DATA + " LIKE '%" + bucket + "%",
                        null,
                        MediaStore.Images.Media.DATE_ADDED + " DESC"
                );
                if (cursor != null) {
                    final int indexId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                    final int indexFilename = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                    while(cursor.moveToNext()) {
                        Uri uri = ContentUris.withAppendedId(uriExternal, cursor.getLong(indexId));
                        CustomGalleryModel model = new CustomGalleryModel(cursor.getLong(indexId), uri, cursor.getString(indexFilename));
                        tmpFiles.add(model);
                    }
                    cursor.close();
                    files.postValue(tmpFiles);
                }
            }
        });
    }

    public void setSelectedFiles(List<CustomGalleryModel> files) {
        executor.execute(() -> {
            selectedFiles.postValue(files);
        });
    }

    public void addSelectedFiles(CustomGalleryModel file) {
        executor.execute(() -> {
            final List<CustomGalleryModel> tmpFiles = new ArrayList<>();
            if (getSelectedFiles().getValue() != null) {
                tmpFiles.addAll(getSelectedFiles().getValue());
            }
            tmpFiles.add(file);
            selectedFiles.postValue(tmpFiles);
        });
    }

    public void removeSelectedFiles(CustomGalleryModel file) {
        executor.execute(() -> {
            final List<CustomGalleryModel> tmpFiles = new ArrayList<>();
            if (getSelectedFiles().getValue() != null) {
                tmpFiles.addAll(getSelectedFiles().getValue());
            }
            int index = -1;
            for (int i = 0; i < tmpFiles.size(); i++) {
                if (tmpFiles.get(i).getId() == file.getId()) {
                    index = i;
                }
            }
            if (index != -1) {
                tmpFiles.remove(index);
                selectedFiles.postValue(tmpFiles);
            }
        });
    }

    public LiveData<List<CustomGalleryModel>> getSelectedFiles() {
        return selectedFiles;
    }
}

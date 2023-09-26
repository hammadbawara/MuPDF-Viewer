package com.hz_apps.books.main;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;

import com.artifex.mupdf.viewer.DocumentActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends Activity
{
	protected final int FILE_REQUEST = 42;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void onStart() {
		super.onStart();
		openPDFInExternalApp("Math10(Sc)EM.pdf");
		this.finish();
	}
	
	private void openPDFInExternalApp(String fileName) {
		// Copy the PDF file from the assets folder to the app's cache directory

		File tempFile = new File(getFilesDir(), fileName);
		if (!tempFile.exists()) {
			try{
				copyFileFromAssetToCache(fileName, tempFile);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		// Create an intent to open the PDF file
		Intent intent = new Intent(this, DocumentActivity.class);
		intent.setAction(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
		intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		intent.putExtra(getComponentName().getPackageName() + ".ReturnToLibraryActivity", 1);
		Uri pdfUri = Uri.fromFile(tempFile);
		intent.setDataAndType(pdfUri, "application/pdf");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);

	}

	private void copyFileFromAssetToCache(String fileName, File tempFile) throws IOException {
		AssetManager assetManager = getAssets();
		InputStream inputStream = assetManager.open(fileName);
		OutputStream outputStream = new FileOutputStream(tempFile);
		byte[] buffer = new byte[1024];
		int read;
		while ((read = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, read);
		}
		outputStream.flush();
		inputStream.close();
		outputStream.close();
	}

	private void launchPdfPicker() {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("*/*");
		intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {
				// open the mime-types we know about
				"application/pdf",
				"application/vnd.ms-xpsdocument",
				"application/oxps",
				"application/x-cbz",
				"application/vnd.comicbook+zip",
				"application/epub+zip",
				"application/x-fictionbook",
				"application/x-mobipocket-ebook",
				// ... and the ones android doesn't know about
				"application/octet-stream"
		});

		startActivityForResult(intent, FILE_REQUEST);
	}

	public void onActivityResult(int request, int result, Intent data) {
		if (request == FILE_REQUEST && result == Activity.RESULT_OK) {
			if (data != null) {
				Intent intent = new Intent(this, DocumentActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
				intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
				intent.setAction(Intent.ACTION_VIEW);
				intent.setDataAndType(data.getData(), data.getType());
				intent.putExtra(getComponentName().getPackageName() + ".ReturnToLibraryActivity", 1);
				startActivity(intent);
			}
			finish();
		} else if (request == FILE_REQUEST && result == Activity.RESULT_CANCELED) {
			finish();
		}
	}
}

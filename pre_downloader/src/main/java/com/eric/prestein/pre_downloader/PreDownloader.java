package com.eric.prestein.pre_downloader;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eric.prestein.pre_downloader.connection.ConnectionDetector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
public class PreDownloader {
    private Context context;
    private String downloadUrl;
    private String outputFileName;
    private String outputDirectory;
    private ProgressBar progressBar;
    private Boolean canToastMessage;
    private String ConnectionFailedMessage="";
    private String ErrorMessage="";
    private String FileName;
    public PreDownloader(Context context) {
        this.context = context;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setCanToastMessage(Boolean canToastMessage) {
        this.canToastMessage = canToastMessage;
    }

    public void setConnectionFailedMessage(String connectionFailedMessage) {
        ConnectionFailedMessage = connectionFailedMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    private class TaskIsInternetAvailable extends AsyncTask<String, Void, Boolean> {
        protected Boolean doInBackground(String... args) {
            return ConnectionDetector.isInternetAvailable(context);
        }
        protected void onPostExecute(Boolean result) {
            if (result){
                new DawnloadTask().execute(downloadUrl);
            }else {
                if (canToastMessage){
                    if (!ConnectionFailedMessage.equals("")){
                        Toast.makeText(context,ConnectionFailedMessage, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context,"Connection failed, please try again later", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
    }
    private class DawnloadTask extends AsyncTask<String,Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                InputStream input=null;
                OutputStream output=null;
                HttpURLConnection connection=null;
                try {
                    URL url=new URL(strings[0]);
                    connection=(HttpURLConnection) url.openConnection();
                    connection.connect();
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                        return "Sever returned HTTP "+connection.getResponseCode()+ " "
                                + connection.getResponseMessage();
                    }
                    int fileLenght=connection.getContentLength();

                    input=connection.getInputStream();
                    output=new FileOutputStream(FileName);
                    byte data[]=new byte[4096];
                    long total=0;
                    int count;
                    while((count = input.read(data)) != -1){
                        if (isCancelled()){
                            return null;
                        }
                        total +=count;
                        if (fileLenght > 0) {
                            publishProgress((int) (total * 100 /fileLenght));
                        }
                        output.write(data,0,count);
                    }
                }catch (Exception e){
                    return e.toString();
                }finally {
                    try{
                        if (output !=null){
                            output.close();
                        }
                        if (input!=null){
                            input.close();
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    if (connection !=null){
                        connection.disconnect();
                    }
                }
            }finally {

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressBar!=null){
                progressBar.setIndeterminate(true);
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (progressBar!=null){
                progressBar.setIndeterminate(false);
                progressBar.setMax(100);
                progressBar.setProgress(values[0]);
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                if (canToastMessage){
                    Toast.makeText(context,s+":  "+ErrorMessage, Toast.LENGTH_SHORT).show();
                }
              }
        }
    }
    public String getFilename(String fileName,String outputDirectory)
    {
        String FILE_NAME="";
        String IMAGE_FINAL_PATH="";
        try {
            String filePath= Environment.getExternalStorageDirectory().getAbsolutePath();
            filePath+= outputDirectory;

            if (new File(filePath).exists())
            {
                FILE_NAME=fileName;
                IMAGE_FINAL_PATH=new File(filePath)+"/"+FILE_NAME;
            }
            else
            {
                new File(filePath).mkdirs();
                FILE_NAME=fileName;
                IMAGE_FINAL_PATH=new File(filePath)+"/"+FILE_NAME;
            }
        }catch (Exception e){
           e.printStackTrace();
        }
        return IMAGE_FINAL_PATH;
    }
    public void startDownload(){
        FileName = getFilename(outputFileName, outputDirectory);
        new TaskIsInternetAvailable().execute();
    }
}

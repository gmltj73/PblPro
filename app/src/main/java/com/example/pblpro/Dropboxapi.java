package com.example.pblpro;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Dropboxapi {
    private static final String ACCESS_TOKEN = "XEHpRFQuqAAAAAAAAAAAEa7sv8T_Hvul62d_gG-THDjKvdKbVpMVfy07HI_QzrUA";


    public static void createaccount() throws DbxException, IOException {

        //경로
        final String FILE_NAME = "test.txt";
        final String DIR_NAME = "data";

        // Create Dropbox client
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        final DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

       // Test - Get current account info
        FullAccount account = client.users().getCurrentAccount();
        System.out.println("============test : " + account.getName().getDisplayName());

        // Get files and folder metadata from Dropbox root directory
        ListFolderResult result = client.files().listFolder("");
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                System.out.println(metadata.getPathLower());
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }


        // Upload "test.txt" to Dropbox
        try (InputStream in = new FileInputStream("/storage/emulated/0/demo/input.cpabe")) {
            FileMetadata metadata = client.files().uploadBuilder("/test.txt")
                    .uploadAndFinish(in);
        }
    }
}




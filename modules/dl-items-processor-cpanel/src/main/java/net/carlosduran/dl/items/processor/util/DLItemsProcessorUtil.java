package net.carlosduran.dl.items.processor.util;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLVersionNumberIncrease;
import com.liferay.document.library.kernel.service.DLAppHelperLocalService;
import com.liferay.document.library.kernel.service.DLAppServiceUtil;
import com.liferay.document.library.kernel.service.DLFileEntryLocalService;
import com.liferay.document.library.kernel.service.DLFileEntryLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import org.apache.commons.io.IOUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class DLItemsProcessorUtil {

    public static void downloadDLItems(Set<String> urls) {
        for(String url : urls) {
            try {
                File downloadDirectory = new File(getDownloadsPath() + getItemDownloadPath(url));
                if(!downloadDirectory.exists()) {
                    if (!downloadDirectory.mkdirs()) {
                        throw new IOException("Unable to create download directory " + downloadDirectory.getAbsolutePath());
                    }
                    transferDLItem(url, downloadDirectory);
                }
            } catch (Exception e) {
                _log.error(e.getMessage());
            }
        }

    }

    private static void transferDLItem(String url, File downloadDirectory) throws FileNotFoundException {
        try (InputStream inputStream = new URL(url).openStream()) {
            Path downloadPath = Paths.get(downloadDirectory.getAbsolutePath());
            Path filePath = downloadPath.resolve(getDLItemFilename(url));
            Files.copy(inputStream, filePath);
        } catch (FileNotFoundException fnfe) {
            throw fnfe;
        } catch (Exception e) {
            _log.error(e);
        }
    }

    private static String getItemDownloadPath(String url) {
        return  File.separator + getDLItemGroupId(url) + File.separator + getDLItemUUID(url);
    }

    private static String[] getDLStructure(String url) {
        int start = url.indexOf("/documents/");
        int docsLength = "/documents/".length();
        return url.substring(start + docsLength).split("/");
    }

    private static String getDLItemGroupId (String url) {
        return getDLStructure(url)[0];
    }

    private static String getDLItemFilename (String url) {
        String[] structure = getDLStructure(url);
        return structure[structure.length - 2];
    }

    private static String getDLItemUUID (String url) {
        String[] structure = getDLStructure(url);
        String uuid = structure[structure.length - 1];
        return (uuid.contains("?")) ? uuid.substring(0, uuid.indexOf("?")) : uuid;
    }

    public static File getOperationDirectory() {
        return new File(System.getProperty("java.io.tmpdir") + "/dl-items-processor/");
    }

    public static void generateZipFile() throws IOException {
        File zipDirectory = new File(getZipDirectoryPath());
        try {
            if(!zipDirectory.mkdirs()) {
                throw new IOException("Cannot create zip directory");
            }
            try (FileOutputStream fileOutputStream = new FileOutputStream(getZipFilePath())) {
                try (ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {
                    File source = new File(getDownloadsPath());
                    for (File childFile : Objects.requireNonNull(source.listFiles())) {
                        compressFileOrDirectory(childFile, childFile.getName(), zipOutputStream);
                    }
                    zipOutputStream.closeEntry();
                    _log.info("generado fichero " + getZipFilePath());
                }
            }
        } catch (Exception e) {
            _log.error(e);
        }
    }

    private static String getZipDirectoryPath() {
        File operationDirectory = getOperationDirectory();
        return operationDirectory.getAbsolutePath() + File.separator + "zip";
    }

    private static String getZipFilePath() {
        return getZipDirectoryPath() + File.separator  + "dl_items.zip";
    }

    private static String getDownloadsPath() {
        File operationDirectory = getOperationDirectory();
        return operationDirectory.getAbsolutePath() + File.separator + "downloads";
    }

    private static void compressFileOrDirectory(File file, String base, ZipOutputStream zos) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File child : Objects.requireNonNull(files)) {
                compressFileOrDirectory(child, base + File.separator + child.getName(), zos);
            }
        } else {
            byte[] buffer = new byte[1024];
            FileInputStream fis = new FileInputStream(file);
            zos.putNextEntry(new ZipEntry(base));

            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            fis.close();
            zos.closeEntry();
            //_log.info("File " + file.getAbsolutePath() + " added");
        }
    }

    public static boolean downloadZipFile(
            ResourceRequest resourceRequest, ResourceResponse resourceResponse) {
        resourceResponse.setContentType(ContentTypes.APPLICATION_ZIP);
        resourceResponse.setProperty("Content-Disposition", "attachment;filename=dl-items.zip");

        try (OutputStream outputStream = resourceResponse.getPortletOutputStream()) {
            try (InputStream inputStream = Files.newInputStream(Paths.get(getZipFilePath()))) {
                IOUtils.copy(inputStream, outputStream);
                outputStream.flush();
                return false;
            }
        } catch (Exception e) {
            _log.error(e);
        }
        return true;
    }
    private static final Log _log = LogFactoryUtil.getLog(DLItemsProcessorUtil.class);

    public static boolean deleteRecursive(File file) throws IOException {
        if(file.exists()) {
            if(file.isDirectory()) {
                for(File childFile : Objects.requireNonNull(file.listFiles())) {
                    deleteRecursive(childFile);
                }
            } else {
                if(!file.delete()) {
                    throw new IOException("Cannot delete " + file.getAbsolutePath());
                }
            }
        }
        return file.delete();
    }

    public static boolean getBoolean(PortletRequest portletRequest, String key) {
        PortletSession portletSession = portletRequest.getPortletSession();
        boolean value = GetterUtil.getBoolean(portletSession.getAttribute(key));
        portletSession.removeAttribute(key);
        return value;
    }

    public static void generateOperationDirectory() {
        File operationDirectory = getOperationDirectory();
        _log.info(operationDirectory.getAbsolutePath());
        try {
            if(operationDirectory.exists()) {
                if(!deleteRecursive(operationDirectory)) {
                    throw new IOException("Problem cleaning operation directory");
                }
            }
            if (!operationDirectory.mkdirs()) {
                throw new IOException("Problem generating operation directory");
            }
        } catch (Exception e) {
            _log.error(e);
        }
    }

    public static Set<String> getFilteredUrls(UploadPortletRequest uploadPortletRequest) {
        Set<String> urls = new HashSet<>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(uploadPortletRequest.getFileAsStream("urls")))) {
            String url;
            int items = 0;
            while (Validator.isNotNull(url = reader.readLine())) {
                if (!Validator.isBlank(url.trim())) {
                    items++;
                    if (url.contains("?")) {
                        url = url.substring(0, url.indexOf("?"));
                    }
                    _log.info("Adding " + url);
                    urls.add(url);
                }
            }
            _log.info("URL's procesadas: " + items);
            _log.info("URL's filtradas: " + urls.size());
        } catch (Exception e) {
            _log.error(e);
        }

        return urls;
    }

    public static void unzip(UploadPortletRequest uploadPortletRequest) {
        try(InputStream inputStream = uploadPortletRequest.getFileAsStream("zipFile")) {
            try (ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
                ZipEntry zipEntry;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if(isValidZipEntry(zipEntry)) {
                        String nombreArchivo = zipEntry.getName();
                        _log.info("Encontrado " + nombreArchivo);

                        String rutaCompleta = getOperationDirectory() + File.separator + "unzipped" + File.separator + nombreArchivo;

                        File itemDirectory = new File(new File(rutaCompleta).getParent());

                        itemDirectory.mkdirs();

                        try (FileOutputStream fileOutputStream = new FileOutputStream(rutaCompleta)) {
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = zipInputStream.read(buffer)) > 0) {
                                fileOutputStream.write(buffer, 0, len);
                            }
                        }
                    }



                    zipInputStream.closeEntry();
                }
            }
        } catch (Exception e) {

        }
    }

    private static boolean isValidZipEntry(ZipEntry zipEntry) {
        String[] entryPath = zipEntry.getName().split("/");
        return (entryPath.length == 3 && entryPath[2].trim().length() > 0);
    }

    public static void uploadItems() {
        File baseDirectory = new File(getOperationDirectory() + File.separator + "unzipped");
        uploadItems(baseDirectory);

    }

    private static void uploadItems(File baseDirectory) {
        Map<Long, List<String>> itemsMap = new HashMap<>();

        Arrays.stream(baseDirectory.listFiles())
                .forEach(groupDirectory -> {
                    long groupId = Long.parseLong(groupDirectory.getName());

                    Arrays.stream(groupDirectory.listFiles())
                            .forEach(uuidDirectory -> {
                                String uuid = uuidDirectory.getName();
                                File dlItem = uuidDirectory.listFiles()[0];
                                try {
                                    DLFileEntry dlFileEntry =
                                            DLFileEntryLocalServiceUtil
                                                    .getDLFileEntryByUuidAndGroupId(uuid, groupId);
                                    _log.info("Encontrado " + dlFileEntry.getFileName());
                                    uploadFileToDLFileEntry(dlItem, dlFileEntry);
                                    _log.info("Subido " + dlFileEntry + " v." + dlFileEntry.getVersion());
                                } catch (Exception e) {
                                    _log.error(e.getMessage());
                                }
                            });
                });
    }

    private static void uploadFileToDLFileEntry(File dlItem, DLFileEntry dlFileEntry) {
        ServiceContext serviceContext = new ServiceContext();
        try (InputStream inputStream = Files.newInputStream(dlItem.toPath())){
            DLAppServiceUtil.updateFileEntry(
                    dlFileEntry.getFileEntryId(),
                    dlFileEntry.getFileName(),
                    dlFileEntry.getMimeType(),
                    dlFileEntry.getTitle(),
                    dlFileEntry.getDescription(),
                    null,
                    DLVersionNumberIncrease.AUTOMATIC,
                    inputStream,
                    dlItem.length(),
                    serviceContext
            );
        } catch (Exception e) {
            _log.error(e);
        }
    }

}

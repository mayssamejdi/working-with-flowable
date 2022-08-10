package poc.inetum.flowable.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class DirectoryTraverser {

    public static Set<File> getFileUris(final File startDir) throws FileNotFoundException {
        checkDirectories(startDir); // throw exception if not valid.
        return getUrisRecursive(startDir);
    }

    /**
     * Gets the uris recursive.<br/>
     * Recursively traverse each directory and uris of files.
     *
     * @param startDir the a starting dir
     * @return the uris recursive
     * @throws FileNotFoundException the file not found exception
     */
    private static Set<File> getUrisRecursive(final File startDir) throws FileNotFoundException {
        final Set<File> sortedSetOfFiles = new HashSet<>();
        final File[] filesAndDirs = startDir.listFiles();
        final List<File> filesDirs = Arrays.asList(filesAndDirs);
        final Iterator<File> filesDirsItr = filesDirs.iterator();
        while (filesDirsItr.hasNext()) {
            final File file = filesDirsItr.next();
            sortedSetOfFiles.add(file); // Add files and directory URIs both
            // If uri is a directory the revisit it recursively.
            if (!file.isFile()) {
                // Call 'getUrisRecursive' to extract uris from directory
                final Set<File> innerSet = getUrisRecursive(file);
                sortedSetOfFiles.addAll(innerSet);
            }
        }

        return sortedSetOfFiles;
    }


    /**
     * Checks if is valid directory.<br/>
     * If directory exists then it is valid. If directory is valid then it can
     * be read.
     *
     * @param directoryUri the a directory
     * @throws FileNotFoundException the file not found exception
     */
    private static void checkDirectories(final File directoryUri) throws FileNotFoundException {
        if (directoryUri == null) {
            throw new IllegalArgumentException("Directory should not be null.");
        }
        if (!directoryUri.exists()) {
            throw new FileNotFoundException("Directory does not exist: " + directoryUri);
        } else if (!directoryUri.isDirectory()) {
            throw new IllegalArgumentException("Is not a directory: " + directoryUri);
        } else if (!directoryUri.canRead()) {
            throw new IllegalArgumentException("Directory cannot be read: " + directoryUri);
        }
    }

    private DirectoryTraverser() {
        super();
    }

}

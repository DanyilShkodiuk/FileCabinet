import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

interface Folder {
    String getName();
    String getSize();
}

interface MultiFolder extends Folder {
    List<Folder> getFolders();
}

interface Cabinet {
    Optional<Folder> findFolderByName(String name);
    List<Folder> findFoldersBySize(String size);
    int count();
}

public class FileCabinet implements Cabinet {

    private final List<Folder> folders;

    public FileCabinet(List<Folder> folders) {
        this.folders = folders;
    }

    @Override
    public Optional<Folder> findFolderByName(String name) {
        return findByName(folders, name);
    }

    private Optional<Folder> findByName(List<Folder> folders, String name) {
        for (Folder folder : folders) {
            if (folder.getName().equals(name)) {
                return Optional.of(folder);
            }

            if (folder instanceof MultiFolder) {
                Optional<Folder> found =
                        findByName(((MultiFolder) folder).getFolders(), name);

                if (found.isPresent()) {
                    return found;
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Folder> findFoldersBySize(String size) {
        List<Folder> result = new ArrayList<>();
        findBySize(folders, size, result);
        return result;
    }

    private void findBySize(List<Folder> folders, String size, List<Folder> result) {
        for (Folder folder : folders) {
            if (folder.getSize().equals(size)) {
                result.add(folder);
            }

            if (folder instanceof MultiFolder) {
                findBySize(((MultiFolder) folder).getFolders(), size, result);
            }
        }
    }

    @Override
    public int count() {
        return countFolders(folders);
    }

    private int countFolders(List<Folder> folders) {
        int total = 0;

        for (Folder folder : folders) {
            total++;

            if (folder instanceof MultiFolder) {
                total += countFolders(((MultiFolder) folder).getFolders());
            }
        }

        return total;
    }
}
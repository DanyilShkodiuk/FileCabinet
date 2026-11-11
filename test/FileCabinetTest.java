import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileCabinetTest {

    record SimpleFolder(String getName, String getSize) implements Folder {
    }

    record NestedFolder(String getName, String getSize, List<Folder> getFolders) implements MultiFolder {
    }

    private Folder invoices;
    private Folder photos;
    private Folder archive2023;
    private FileCabinet cabinet;

    /*
     * documents (MEDIUM)
     * ├── invoices (SMALL)
     * └── archive (LARGE)
     *     └── 2023 (SMALL)
     * photos (LARGE)
     */
    @BeforeEach
    void setUp() {
        invoices = new SimpleFolder("invoices", "SMALL");
        archive2023 = new SimpleFolder("2023", "SMALL");
        Folder archive = new NestedFolder("archive", "LARGE", List.of(archive2023));
        Folder documents = new NestedFolder("documents", "MEDIUM", List.of(invoices, archive));
        photos = new SimpleFolder("photos", "LARGE");
        cabinet = new FileCabinet(List.of(documents, photos));
    }

    @Test
    void findsTopLevelFolderByName() {
        assertSame(photos, cabinet.findFolderByName("photos").orElseThrow());
    }

    @Test
    void findsDeeplyNestedFolderByName() {
        assertSame(archive2023, cabinet.findFolderByName("2023").orElseThrow());
    }

    @Test
    void returnsEmptyOptionalWhenNameIsMissing() {
        Optional<Folder> result = cabinet.findFolderByName("music");

        assertTrue(result.isEmpty());
    }

    @Test
    void findsFoldersBySizeAtEveryLevel() {
        List<Folder> small = cabinet.findFoldersBySize("SMALL");

        assertEquals(List.of(invoices, archive2023), small);
    }

    @Test
    void findsMultiFoldersBySizeToo() {
        List<String> large = cabinet.findFoldersBySize("LARGE").stream().map(Folder::getName).toList();

        assertEquals(List.of("archive", "photos"), large);
    }

    @Test
    void countsAllFoldersIncludingNested() {
        assertEquals(5, cabinet.count());
    }

    @Test
    void handlesEmptyCabinet() {
        FileCabinet empty = new FileCabinet(List.of());

        assertEquals(0, empty.count());
        assertTrue(empty.findFolderByName("anything").isEmpty());
        assertTrue(empty.findFoldersBySize("SMALL").isEmpty());
    }
}

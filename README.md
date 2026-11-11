# FileCabinet

[![CI](https://github.com/DanyilShkodiuk/FileCabinet/actions/workflows/ci.yml/badge.svg)](https://github.com/DanyilShkodiuk/FileCabinet/actions/workflows/ci.yml)

A small Java exercise in modelling a tree of folders with interfaces and searching it recursively.

## The task

A cabinet holds folders. A folder has a name and a size label (both strings). A `MultiFolder` is a folder that contains other folders, so the cabinet is a tree of any depth. Implement `Cabinet`:

```java
interface Cabinet {
    Optional<Folder> findFolderByName(String name); // first folder with this name, at any depth
    List<Folder> findFoldersBySize(String size);    // every folder of this size, at any depth
    int count();                                    // total number of folders, nested ones included
}
```

## Solution

[`FileCabinet`](src/FileCabinet.java) walks the tree with depth-first recursion. Each method checks the current folder, and if it is a `MultiFolder`, descends into its children:

- `findFolderByName` returns as soon as a match is found, wrapped in `Optional`, instead of returning `null`
- `findFoldersBySize` collects matches into one shared result list, so no intermediate lists are created
- `count` adds one for each folder plus the count of its children

The public methods stay small; the recursion lives in private helpers that take the current list of folders.

**Concepts:** interfaces and interface inheritance, recursion, `Optional`, `instanceof`, composite pattern.

## Tests

7 JUnit 5 tests in [`test/FileCabinetTest.java`](test/FileCabinetTest.java) build a small tree (folders inside folders inside folders) and check search by name at any depth, a missing name, search by size across levels, counting nested folders and an empty cabinet. Test folders are Java `record`s implementing the interfaces.

```bash
mvn test   # requires JDK 17+
```

GitHub Actions runs them on every push and pull request.

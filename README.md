# FileCabinet

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

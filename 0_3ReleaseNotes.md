#Release notes for the 0.3 version

# Changes #

  * Several general bug fixes
  * All new Introspector that does a better job working with the GWT compiler.

The new Introspector has lost class-literal lookup. I don't think this is actually used by many people, and in terms of the Gwittir internals was completely unused.

The object-based lookup now blocks on instanceof checks so the compiler can prune the object list.

It also does late-construction of the BeanDescriptor objects which should help startup time in large apps.

Finally, all the Method classes have been moved out of an array and into a separate class so they can be pruned properly.
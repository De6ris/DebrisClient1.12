package com.github.debris.debrisclient.inventory.section;

public interface IContainer {
    void dc$setSectionHandler(SectionHandler sectionHandler);

    SectionHandler dc$getSectionHandler();

    String dc$getTypeString();
}

import {Injectable} from '@angular/core';
import {Tab} from './tab.model';
import {UnitService} from "../unit/unit.service";
import {Unit} from "../unit/unit.model";

@Injectable()
export class TabService {

  unitTabs: Tab[];
  courseTabs: Tab[];
  lessonTabs: Tab[];

  activeTab: Tab;

  constructor(public unitService: UnitService) {
    this.unitTabs = [];
    this.courseTabs = [];
    this.lessonTabs = [];
  }

  addTab(t: Tab) {
    this.deactivateTabs();
    let toAdd = true;
    if (t.type === 'Unidad') {
      for (let tab of this.unitTabs) {
        if (+tab.unitId === +t.unitId) {
          toAdd = false;
          tab.isActive = true;
          this.activeTab = tab;
        }
      }
      if (toAdd) {
        this.unitTabs.push(t);
        this.activeTab = t;
      }
      this.updateActiveTabLink('Unidad', t.id, t.name, null, null, null);
      this.unitService.getUnambiguousName(t.id).subscribe((unambiguousNameUnit: Unit) => {
        t._name = unambiguousNameUnit.name;
        for (let tab of this.unitTabs) {
          if (+tab.unitId === +t.unitId) {
            tab._name = t.name;
          }
        }
      });
    } else if (t.type === 'Curso') {
      for (let tab of this.courseTabs) {
        if (+tab.id === +t.id && tab.name === t.name) {
          toAdd = false;
          tab.isActive = true;
          this.activeTab = tab;
        }
      }
      if (toAdd) {
        this.courseTabs.push(t);
        this.activeTab = t;
      }
    } else if (t.type === 'Lección') {
      for (let tab of this.lessonTabs) {
        if (+tab.id === +t.id && tab.name === t.name) {
          toAdd = false;
          tab.isActive = true;
          this.activeTab = tab;
        }
      }
      if (toAdd) {
        this.lessonTabs.push(t);
        this.activeTab = t;
      }
    }
  }

  removeTab(tab: Tab) {
    const unitTabsIndex: number = this.unitTabs.indexOf(tab);
    if (unitTabsIndex !== -1) {
      this.unitTabs.splice(unitTabsIndex, 1);
    }
    const courseTabsIndex: number = this.courseTabs.indexOf(tab);
    if (courseTabsIndex !== -1) {
      this.courseTabs.splice(courseTabsIndex, 1);
    }
    const lessonTabsIndex: number = this.lessonTabs.indexOf(tab);
    if (lessonTabsIndex !== -1) {
      this.lessonTabs.splice(lessonTabsIndex, 1);
    }
  }

  activateTab(t: Tab) {
    if (t.type === "Unidad") {
      for (let tab of this.unitTabs) {
        if (+tab.id === +t.id && tab.name === t.name) {
          tab.isActive = true;
          this.activeTab = tab;
        } else {
          tab.isActive = false;
        }
      }
    } else if (t.type === "Curso") {
      for (let tab of this.courseTabs) {
        if (+tab.id === +t.id && tab.name === t.name) {
          tab.isActive = true;
          this.activeTab = tab;
        } else {
          tab.isActive = false;
        }
      }
    } else if (t.type === "Lección") {
      for (let tab of this.lessonTabs) {
        if (+tab.id === +t.id && tab.name === t.name) {
          tab.isActive = true;
          this.activeTab = tab;
        } else {
          tab.isActive = false;
        }
      }
    }
  }

  deactivateTabs() {
    this.activeTab = null;
    for (let tab of this.unitTabs) {
      tab.isActive = false;
    }
    for (let tab of this.courseTabs) {
      tab.isActive = false;
    }
    for (let tab of this.lessonTabs) {
      tab.isActive = false;
    }
  }

  updateActiveTabLink(type: string, id: number, name: string, unitId: string, courseId: number, moduleId: number) {
    for (let tab of this.unitTabs) {
      if (tab.id === this.activeTab.id && tab.name === this.activeTab.name) {
        tab.updateLink(type, id, name, unitId, courseId, moduleId);
      }
    }
  }

  updateCourseActiveTabLink(type: string, id: number, name: string, unitId: string, courseId: number, moduleId: number) {
    for (let tab of this.courseTabs) {
      if (tab.id === this.activeTab.id && tab.name === this.activeTab.name) {
        tab.updateLink(type, id, name, unitId, courseId, moduleId);
      }
    }
  }

  emptyTabs() {
    this.courseTabs = [];
    this.unitTabs = [];
    this.lessonTabs = [];
  }

}

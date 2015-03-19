This is the missing content of the article:

**Representing Dynamic Invariants in Ontologically Well-Founded Conceptual Models**

by **John Guerson** and **João Paulo A. Almeida**.


---

**Section 3.2** World-Reified Model

Actual Multiplicity Constraints:
```
context World 

inv marriage_mediates_one_wife_at_a_time: 
    self.individual->select(i | i.oclIsKindOf(Marriage))->forAll(m |
    m.mediates_marriage_wife->select(r | r.world = self)->size() = 1)

inv marriage_mediate_one_husband_at_a_time: 
    self.individual->select(i | i.oclIsKindOf(Marriage))->forAll(m | 
    m.mediates_marriage_husband->select(r | r.world = self)->size() = 1)

inv wife_mediated_by_one_marriage_at_a_time: 
    self.individual->select(i| i.oclIsKindOf(Wife))->forAll(h | 
    h.mediates_marriage_wife->select(r | r.world = self)->size() = 1)

inv husband_mediated_by_one_marriage: 
    self.individual->select(i| i.oclIsKindOf(Husband))->forAll(h | 
    h.mediates_marriage_husband->select(r | r.world = self)->size() = 1)
```

Cycle Constraints:
```
context mediates_Marriage_Wife 

inv no_cycle_between_wife_world_mediation: 
    self.world.individual->select(i |i.oclIsKindOf(Wife))->includes(self.wife)

inv no_cycle_between_marriage_world_mediation: 
    self.world.individual->select(i|i.oclIsKindOf(Marriage))->includes(self.marriage)
```

Immutability and Duplicability Constraints:
```
context mediates_Marriage_Wife 

inv wife_immutability: 
    mediates_Marriage_Wife.allInstances()->forAll(m | m<>self and
    m.marriage<>self.marriage implies m.wife = self.wife)

inv no_duplicability: 
    mediates_Marriage_Wife.allInstances()->forAll(m | m<>self implies not
    (m.marriage=self.marriage and m.wife=self.wife and m.world=self.world))
```


---

**Section 3.4** World Structure

Additional OCL Constraints
```
context World 
inv no_cycle: 
    self->asSet()->closure(next)->excludes(self) 

context Path 
inv no_parallel_structure:
    Path.allInstances()->forAll(p|self.world->intersection(p.world)->notEmpty()) 

context Path 
inv one_terminal: self.world->one(w | w.next->isEmpty())

inv one_origin: self.world->one(w | w.previous.oclIsUndefined())

inv no_two_paths_with_same_terminal_world: 
    Path.allInstances()->forAll(p | p<>self implies p.world->select( 
    w | w.next->isEmpty()) <> self.world->select(w | w.next->isEmpty()))

inv each_terminal_world_has_one_path: 
    let ts: Set(World) = World.allInstances()->select(w |w.next->isEmpty()) 
    in ts->forAll(t| Path.allInstances()->one(p | p.world->includes(t)))

inv worlds_of_a_path_derived_from_terminal_worlds:
    let t: Set(World) = self.world->select(w| w.next->isEmpty()) 
    in (self.world-t) = t->closure(previous)

```

Implementation of Temporal Pre-Defined Operations
```
context World::next():Set(World) body: self.next 

context World::previous():World body: self.previous

context World::paths():Set(Path) body: self.path

context Path::worlds():Set(World) body: self.world

context World::hasNext():Boolean body: not self.next->isEmpty()

context World::hasPrevious():Boolean body: not self.previous.oclIsUndefined()

context World::allIndividuals():Set(Individual) body: self.individual

context World::isOrigin():Boolean body: self.previous.oclIsUndefined()

context World::isTerminal():Boolean body: self.next->isEmpty()

context Individual::existsIn(w: World):Boolean body: w.individual->includes(self)

context World::allNext():Set(World) body: self->asSet()->closure(next)->asSet()

context World::allNext(w: World):Set(World) 
body: if self.allNext()->includes(w) then w.allPrevious()–self.allPrevious()–self 
      endif else Set{}

context World::allNext(p: Path):Set(World) 
body: self->asSet()->closure(next)->asSet()->select(w | w.paths()->includes(p))

context World::allPrevious():Set(World) 
body: self->asSet()->closure(previous)->asSet()

context World::allPrevious(w: World):Set(World) 
body: if self.allPrevious()->includes(w) then self.allPrevious()–w.allPrevious()–w 
      endif else Set{}
```

World structure in Alloy:
```
module tocl_world_structure
some sig World {
	next: set World
}

//no joining histories
fact { all w: World | # next.w <=1 }
//no cycle
fact { all w: World | w !in w.^next }
//no parallel structure
fact { all p1,p2: Path | some p1.world & p2.world }

some sig Path { 
    world: some World 
}{
   // one terminal world
	# { w: world | no w.next } = 1
   // one origin world
	# { w: world | no next.w } = 1
}

fact { 
    // no two paths with same terminal worlds
    all p1:Path | all p2: Path | 
    p1!=p2 => {w: p1.world | no w.next } != {w: p2.world | no w.next } 
}

fact { 
    // worlds of a path are equal to past worlds of terminal world
    all p: Path | let t = {w: p.world | no w.next } | 
    (p.world - t) = ^(World<:next).t 
}

fact { 
     // every terminal world of the structure must be contained in a path
     let ts = { w: World | no w.next } | 
     (all t: ts | one p: Path | t in p.world) 
}

run {} for 10 but 7 int
```
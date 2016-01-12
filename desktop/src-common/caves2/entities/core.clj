(ns caves2.entities.core)

(defprotocol Entity
  (tick [this screen]
        "Update the world to handle the passing of a tick for this entity"))

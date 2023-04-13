# Features

Currently the features logic is a little verbose. 

- Call method into features function (ok)
- Reads a file called features.yaml (ok)
- Filters all the features based on the input template and the `status` field in the features.yaml file 
  - This file is used the generate the table and keep feature presentation dry 
  - It might need to be rethought based on how it actually influences what features should be displayed 
- Prompts the user for each feature 
  - Not good because we will wont to allow the devs to show conditional features (ie features that rely on other features being enabled) and feature prompts 
  - What would be better is if the `features` function simply returned data and then features themselves had their logic elsewhere

What should happen is:

- There is a features function with methods to do whatever 
- One of these methods returns a pretty prompt with an input of the `contract_standard` (should refactor to be more specific to template)
- The logic to call each feature then has a manual override 
  - Would require being able to call templated tackle 
    - not possible now but easy to implement and many workarounds

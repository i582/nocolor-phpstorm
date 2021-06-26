<img width="350" alt="image" src="https://user-images.githubusercontent.com/51853996/122410502-a543ff00-cf8c-11eb-9b23-6b0c6e900f1e.png">

# NoColor plugin for PhpStorm

<!-- Plugin description -->
PhpStorm plugin that allows the IDE to understand the [concept of colors](https://github.com/VKCOM/nocolor/blob/master/docs/introducing_colors.md) for PHP.

### Features

- Quick jump to the rule in the palette;
- Autocomplete color names;
- Color checks; 
- Rules checks;
- Tips and checks associated with the special color `remover`.

<!-- Plugin description end -->

## About

The plugin provides various validations and also improves the development experience with color navigation.

Find out more about NoColor tool [here](https://github.com/VKCOM/nocolor).

You can also find this plugin on the official [JetBrains plugins](https://plugins.jetbrains.com/plugin/17054-nocolor) website.

## Installation

- Using IDE built-in plugin system:

  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "NoColor"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/i582/nocolor-phpstorm/releases/latest) and install it manually using
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## Features

- Quick jump to the rule in the palette;

  <img width="295" alt="image" src="https://user-images.githubusercontent.com/51853996/120945069-59998600-c740-11eb-94fb-fd88d4027f92.png">

- Autocomplete color names;

  <img width="500" alt="image" src="https://user-images.githubusercontent.com/51853996/121084637-9077a680-c7e9-11eb-91a8-f453f08a7392.png">
  
- Color checks: 

  - Undefined colors:

    <img width="450" alt="image" src="https://user-images.githubusercontent.com/51853996/121084750-b4d38300-c7e9-11eb-9f19-799e1d0095cc.png">

  - Duplicated colors:

    <img width="600" alt="image" src="https://user-images.githubusercontent.com/51853996/121085486-95892580-c7ea-11eb-8b28-f0cc941d453b.png">

  - Several colors in one `@color` tag:

    <img width="500" alt="image" src="https://user-images.githubusercontent.com/51853996/121085605-c4070080-c7ea-11eb-8262-c351e1089ecc.png">

  - Not allowed 'transparent' color in `@color` tag:

    <img width="450" alt="image" src="https://user-images.githubusercontent.com/51853996/123413008-eb1b4b80-d5ba-11eb-9c48-b77d0cc4470c.png">

- Rules checks:

  - Order:

    <img width="600" alt="image" src="https://user-images.githubusercontent.com/51853996/123413649-b360d380-d5bb-11eb-8574-39e2deaa258e.png">

  - Structure:

    <img width="700" alt="image" src="https://user-images.githubusercontent.com/51853996/121086009-4ee7fb00-c7eb-11eb-9999-2079f59ee3cb.png">

  - Not allowed 'transparent' color in rule:
    
    <img width="450" alt="image" src="https://user-images.githubusercontent.com/51853996/123413387-62e97600-d5bb-11eb-983e-aa6328dc0583.png">
  
- Tips and checks associated with the special color `remover`:

  - `remover` color description:

    <img width="580" alt="image" src="https://user-images.githubusercontent.com/51853996/121084514-6625e900-c7e9-11eb-8780-2ac677cc675a.png">
  
  - Other colors with `remover`:
  
    <img width="650" alt="image" src="https://user-images.githubusercontent.com/51853996/120994544-3ea13300-c78d-11eb-98f2-2ca44eec80b2.png">

## License

This project is under the **MIT License**. See the [LICENSE](https://github.com/i582/nocolor-phpstorm/blob/master/LICENSE) file for the full license text.


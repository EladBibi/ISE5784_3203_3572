Welcome to our 3D Renderer project!
-------------------------------------------------------------------------------------------------------------------------------------------------        

About the Program:
This is a 3D image generator that utilizes a custom-built 3D rendering engine to generate images in PNG format. The project is written from the ground up in Java, with meticulously detailed and well-structured documentation of the entire API. Throughout development, great emphasis was placed on utilizing Agile principles and best design practices such as RDD, TDD, and Pair Programming, with unit tests written at every stage.

The project leverages a substantial amount of theoretical and mathematical knowledge acquired in previous courses.

**Features:**
- A complete 3D rendering engine built from scratch, including an API for fundamental mathematical operations such as vector, ray, and geometry operations.
- A thoroughly documented API following best design practices.
- Full implementation of the Phong reflection model for realistic light reflections and refractions.
- Configurable materials for creating different surface interactions with light (e.g., metal, glass, mirrors).
- A movement system around a pivot point for easily moving and/or cloning geometry objects within the scene.
- Advanced camera movement along user-defined curves: Utilizes Bezier curves and quadratic interpolation to create smooth, curves across 
    three points, enabling video generation where the camera moves fluidly and cinematically along a curve in each frame (see attached videos).
- And more..
    
**Super-sampling Algorithms for Enhanced Image Quality:**
- Anti-Aliasing: Full implementation of the anti-aliasing algorithm for smoothing out the edges of geometries, resulting in more realistic images.
- Glossy & Diffused Surfaces: An advanced algorithm that samples multiple reflection and refraction rays instead of one, 
        creating blurrier and less "perfect" reflections and refractions, contributing to more unique and realistic-looking surfaces 
        (see the reflection of the chess pieces on the chessboard in the attached image).
        
**Acceleration Techniques for Improved Performance:**
- Multithreading: Implemented using a custom pixel-manager class that leverages Java's ExecutorService.
- An extremely advanced scene-partitioning algorithm: As the final and most challenging stage of the project, we implemented the voxel-partitioning algorithm 
    (also known as "regular grids") for massive performance gains. This algorithm divides the scene into 3D voxel cubes and stores geometry references within them 
    accordingly. This allows us to march rays only through the voxels in their path, drastically reducing the number of ray-intersection calculations during the ray-tracing process.

The project received a perfect score of 100.
-------------------------------------------------------------------------------------------------------------------------------------------------  


Image rendered without Image-Enhancing algorithms:
![render 5 - no effects](https://github.com/user-attachments/assets/0158667e-832f-4f8d-89cf-48f8d5e38c46)

The Previous image but rendered with the "Glossy & Diffused Surfaces" effect. 
notice the differences in the blurry refraction of moon through the window and the blurry reflection of the chess pieces on the chessboard as oppose to the previous image
![render 5 - glossy   diffusive surfaces X81](https://github.com/user-attachments/assets/a34673c8-c649-4079-9fb3-3b21e395a3cc)

Rendered with "anti-aliasing" and the "Diffused Surfaces" algorithms.
Again, notice the blurry reflection of the chess peices and the smooth edges of the objects
![voxel tracer, multithreading - aa 9, reflectiveness 81](https://github.com/user-attachments/assets/e9f6fb5e-30ff-47b9-a64f-d4f44753c75d)


Video containing 225 frames and generated with anti-aliasing
- https://github.com/user-attachments/assets/ef3b8f75-0427-4940-ac98-4e10655e42f7

More videos:
- https://github.com/user-attachments/assets/41e5e804-e7a1-4a0c-a2a8-76af3ab02716
- https://github.com/user-attachments/assets/9843661c-6c26-4626-b1cc-0b1d7479bf2d

More images:

![bonusImage5](https://github.com/user-attachments/assets/2025aaa7-a136-4229-a217-0f38af5a52cb)
![bonusImage19](https://github.com/user-attachments/assets/37a613f8-e955-426c-9386-2466e5481772)


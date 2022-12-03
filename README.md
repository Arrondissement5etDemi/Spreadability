# Spreadability
This is an algorithm to extract small-wavevector (long-wavelength) behavior of two-point correlations of two-phase media via the [diffusion spreadability](https://journals.aps.org/pre/abstract/10.1103/PhysRevE.104.054102). It works faster, cleaner and more robust than a direct fit of small wavewectors.

Please see the description of the algorithm in our PRApplied paper, Sec. V:
[Dynamic Measure of Hyperuniformity and Nonhyperuniformity in Heterogeneous Media via the Diffusion Spreadability](https://journals.aps.org/prapplied/abstract/10.1103/PhysRevApplied.17.034022)

Please cite us as:

@article{PhysRevApplied.17.034022,
  title = {Dynamic Measure of Hyperuniformity and Nonhyperuniformity in Heterogeneous Media via the Diffusion Spreadability},
  author = {Wang, Haina and Torquato, Salvatore},
  journal = {Phys. Rev. Applied},
  volume = {17},
  issue = {3},
  pages = {034022},
  numpages = {21},
  year = {2022},
  month = {Mar},
  publisher = {American Physical Society},
  doi = {10.1103/PhysRevApplied.17.034022},
  url = {https://link.aps.org/doi/10.1103/PhysRevApplied.17.034022}
}

## How it works
to compile: `javac SpreadabilityClient.java`
to run: `java -cp . SpreadabilityClient [file_name] [\epsilon value]`
For example:
`java -cp . SpreadabilityClient BCCPhi0x20.spr 1e-6`

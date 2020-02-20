
Pod::Spec.new do |s|
  s.name         = "RNRoamRnToolkit"
  s.version      = "1.0.0"
  s.summary      = "RNRoamRnToolkit"
  s.description  = <<-DESC
                  RNRoamRnToolkit
                   DESC
  s.homepage     = "https://github.com/Roamltd/roam-rn-toolkit/"
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/author/RNRoamRnToolkit.git", :tag => "master" }
  s.source_files  = "RNRoamRnToolkit/**/*.{h,m}"
  s.requires_arc = true


  s.dependency "React"
  #s.dependency "others"

end

  
